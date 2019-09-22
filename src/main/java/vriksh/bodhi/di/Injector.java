package vriksh.bodhi.di;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class with  for validation, and using reflection
 * for reading annotation,constructing new instances
 * and setting instance variables.
 */
public class Injector {

  private final BindingModule module;
  private final Map<Class, Object> singleTons = new HashMap<>();

  Injector(BindingModule module) {
    this.module = module;
  }

  void preCondition() {
    module.superClasses().forEach(
        aClass -> constructorChecks(module.getBoundTypeOrDefault(aClass))
    );
  }

  /**
   * Checks that class has only one
   * public no-args constructor.
   * @param aClass class to check
   */
  private void constructorChecks(Class<?> aClass) {
    Constructor<?>[] constructors = aClass.getDeclaredConstructors();
    if (constructors.length != 1) {
      throw new BodhiException("More than one constructor not allowed.");
    }
    if (!Modifier.toString(constructors[0].getModifiers()).contains("public")) {
      throw new BodhiException("Non public constructor not allowed.");
    }

    if (constructors[0].getParameterCount() != 0) {
      throw new BodhiException("Constructor with args not allowed.");
    }
  }

  /**
   * Gets fields with {@link Inject} annotation
   * @param aClass class whose fields have to be checked
   * @return List of fields with the annotation
   */
  private List<Field> getInjectAnnotatedFields(Class<?> aClass) {
    List<Field> fields = new ArrayList<>();
    Arrays.stream(aClass.getDeclaredFields()).forEach(field -> {
      if (field.getAnnotation(Inject.class) != null) {
        fields.add(field);
      }
    });
    return fields;
  }

  /**
   * Method to be invoked to get new instance of some class.
   * @param clazz The class whose instance is required.
   * @param <T> Type arg
   * @return a new instance of T
   */
  public <T> T getInstance(Class<T> clazz) {
    Class<?> boundType = module.getBoundTypeOrDefault(clazz);
    if (isSingleTon(boundType)) {
      return (T) singleTons.computeIfAbsent(clazz, this::newObject);
    } else {
      Object object = newObject(boundType);
      getInjectAnnotatedFields(clazz).forEach(field -> {
        setInstanceVariable(field, object, getInstance(module.getBoundTypeOrDefault(field.getType())));
      });
      return (T) object;
    }
  }


  private Object newObject(Class<?> clazz) {
    try {
      return clazz.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new BodhiException(e);
    }
  }

  private boolean isSingleTon(Class<?> clazz) {
    return clazz.getAnnotation(Singleton.class) != null;
  }

  /**
   * Method to set an instance variable of a class.
   * @param field the field which has to be set.
   * @param object The object in which field has to be set.
   * @param instanceVariable The value of the field.
   */
  private void setInstanceVariable(Field field, Object object, Object instanceVariable) {
    boolean isAccessible = field.isAccessible();
    if (!isAccessible) {
      field.setAccessible(true);
    }
    try {
      field.set(object, instanceVariable);
    } catch (IllegalAccessException e) {
      throw new BodhiException(e);
    }
    if (!isAccessible) {
      field.setAccessible(false);
    }
  }
}

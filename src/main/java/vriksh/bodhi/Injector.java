package vriksh.bodhi;

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

  private List<Field> getInjectAnnotatedFields(Class<?> aClass) {
    List<Field> fields = new ArrayList<>();
    Arrays.stream(aClass.getDeclaredFields()).forEach(field -> {
      if (field.getAnnotation(Inject.class) != null) {
        fields.add(field);
      }
    });
    return fields;
  }

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

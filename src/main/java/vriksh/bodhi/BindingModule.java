package vriksh.bodhi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BindingModule {

  private final Map<Class, Class> bindings = new HashMap<>();

  protected <T, U extends T> void bind(Class<T> superClass, Class<U> childClass) {
    bindings.put(superClass, childClass);
  }

  protected abstract void define();

  <T> Class<? extends T> getBoundTypeOrDefault(Class<T> superClass) {
    return bindings.getOrDefault(superClass, superClass);
  }

  Collection<Class> superClasses() {
    return bindings.keySet();
  }
}

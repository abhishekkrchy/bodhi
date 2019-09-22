package vriksh.bodhi.di;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which should be implemented
 * for binding the interfaces to
 * implementations. Just keeps a map
 * for bound entries.
 */
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

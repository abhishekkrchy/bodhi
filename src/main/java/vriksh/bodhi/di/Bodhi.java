package vriksh.bodhi.di;

/**
 * The library class which has
 * to be used to get an {@link Injector}
 * by calling {@link #create(BindingModule)}
 */
public class Bodhi {

  private Bodhi() {

  }

  public static Injector create(BindingModule module) {
    module.define();
    Injector injector = new Injector(module);
    injector.preCondition();
    return injector;
  }
}

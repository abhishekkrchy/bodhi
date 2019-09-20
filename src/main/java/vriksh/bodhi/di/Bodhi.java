package vriksh.bodhi.di;

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

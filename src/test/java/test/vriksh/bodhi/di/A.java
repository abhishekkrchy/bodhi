package test.vriksh.bodhi.di;

import vriksh.bodhi.di.BindingModule;
import vriksh.bodhi.di.Bodhi;
import vriksh.bodhi.di.Injector;

public class A {
  public static void main(String[] args) {
    Injector injector = Bodhi.create(new BindingModule() {
      @Override
      protected void define() {
        bind(IC.class, C.class);
        bind(ID.class, D.class);
      }
    });
    injector.getInstance(B.class).test();
    injector.getInstance(B.class).test();
    injector.getInstance(B.class).test();
  }
}

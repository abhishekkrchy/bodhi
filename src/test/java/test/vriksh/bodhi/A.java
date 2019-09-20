package test.vriksh.bodhi;

import vriksh.bodhi.BindingModule;
import vriksh.bodhi.Bodhi;
import vriksh.bodhi.Injector;

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

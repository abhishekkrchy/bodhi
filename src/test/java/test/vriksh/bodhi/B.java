package test.vriksh.bodhi;

import javax.inject.Inject;

public class B {
  @Inject
  private IC c;
  @Inject
  private ID d;

  public void test() {
    System.out.println(c.c() + " " + d.d());
  }
}

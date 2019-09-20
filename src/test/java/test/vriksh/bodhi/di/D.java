package test.vriksh.bodhi.di;

import javax.inject.Singleton;

@Singleton
public class D implements ID {
  @Override
  public String d() {
    return "inside d of " + hashCode();
  }
}

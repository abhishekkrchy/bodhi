package test.vriksh.bodhi.di;

public class C implements IC {
  @Override
  public String c() {
    return "inside c of " + hashCode();
  }
}

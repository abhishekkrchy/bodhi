package test.vriksh.bodhi;

public class C implements IC {
  @Override
  public String c() {
    return "inside c of " + hashCode();
  }
}

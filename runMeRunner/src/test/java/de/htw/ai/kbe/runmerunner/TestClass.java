package de.htw.ai.kbe.runmerunner;

public class TestClass {

    public TestClass() {
    }

    @RunMe
    private void doNothing() {

    }

    @RunMe
    @AnotherAnnotation
    public boolean isOneEqualToOne() {
        return true;
    }

    public String welcome() {
        return "welcome";
    }

    public void foo(TestClassWithRunMes clazz) {
    }

    @RunMe
    protected void foo2(boolean iAmHappy) {
        if (iAmHappy) {
            String str = "I will study for KBE";
        } else {
            String str = "I won't study at all";
        }

    }
}

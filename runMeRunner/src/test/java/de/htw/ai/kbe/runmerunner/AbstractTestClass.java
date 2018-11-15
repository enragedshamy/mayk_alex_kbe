package de.htw.ai.kbe.runmerunner;

public abstract class AbstractTestClass {


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

    @RunMe
    protected abstract void foo2();

}

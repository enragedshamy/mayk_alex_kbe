package de.htw.ai.kbe.runmerunner;

public abstract class AbstractTestClassWithRunMes {

    @RunMe
    private void doNothing() {

    }

    @RunMe
    @AnotherAnnotation
    public boolean isOneEqualToOne(int integer) {
        return true;
    }

    public String welcome() {
        return "welcome";
    }

    @RunMe
    public abstract void foo();
}

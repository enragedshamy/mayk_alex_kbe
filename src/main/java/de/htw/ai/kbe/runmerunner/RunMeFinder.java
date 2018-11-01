package de.htw.ai.kbe.runmerunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class RunMeFinder {

    private final Class clazz;
    private final String fileName;
    private List<Method> methodsWithRunMe;
    private Object obj;

    public RunMeFinder(Class clazz, String fileName) {
        this.clazz = clazz;
        this.fileName = fileName;
        try {
            this.obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            writeToFile("Instantiating class: " + clazz.getName() + "threw an Exception.");
        }
    }

    public void findAnnotatedMethodsAndWriteToFile() {
        List<Method> methodsWithRunMe = new ArrayList<>();
        List<Method> methodsWithOutRunMe = new ArrayList<>();
        HashMap<String, String> methodsGeneratingExceptions = new HashMap<>();

        Stream.of(clazz.getDeclaredMethods())
                .forEach(method -> {
                            method.setAccessible(true);

                            boolean flag = true;
                            for (Annotation annotation : method.getAnnotations()) {
                                if (annotation.annotationType().getName().equals(RunMe.class.getName())) {
                                    methodsWithRunMe.add(method);
                                    flag = false;

                                    try {
                                        method.invoke(obj);
                                    } catch (Exception e) {
                                        methodsGeneratingExceptions.put(method.getName(), e.toString());
                                    }
                                    break;
                                }
                            }
                            if (flag) {
                                methodsWithOutRunMe.add(method);
                            }
                        }
                );

        writeOutputToFile(methodsWithRunMe, methodsWithOutRunMe, methodsGeneratingExceptions);
    }

    private void writeOutputToFile(List<Method> methodsWithRunMe, List<Method> methodsWithOutRunMe, HashMap<String, String> methodsGeneratingExceptions) {
        StringBuilder toWrite = new StringBuilder();

        toWrite.append("Methods without @RunMe:\n");
        methodsWithOutRunMe.forEach(method ->
                toWrite
                        .append("\t")
                        .append(method.getName())
                        .append("\n")
        );

        toWrite.append("\n");

        toWrite.append("Methods with @RunMe:\n");
        methodsWithRunMe.forEach(method ->
                toWrite
                        .append("\t")
                        .append(method.getName())
                        .append("\n")
        );

        toWrite.append("\n");

        toWrite.append("Non-invokable methods:\n");
        methodsGeneratingExceptions.forEach(
                (method, exception) ->
                        toWrite
                                .append("\t")
                                .append(method)
                                .append(": ")
                                .append(exception)
                                .append("\n")
        );

        writeToFile(toWrite.toString());
    }

    private void writeToFile(String stringToWrite) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("report.txt"))) {
            writer.write(stringToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
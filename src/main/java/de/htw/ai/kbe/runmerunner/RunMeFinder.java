package de.htw.ai.kbe.runmerunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RunMeFinder {

    private final Class clazz;
    private final String fileName;

    public RunMeFinder(Class clazz, String fileName) {
        this.clazz = clazz;
        this.fileName = fileName;
    }

    public void findAnnotatedMethodsAndWriteToFile() {
        List<Method> methodsWithRunMe = new ArrayList<>();
        List<Method> methodsWithOutRunMe = new ArrayList<>();
        List<Method> rest = new ArrayList<>();

        System.out.println("\nSearching for methods of Class: " + clazz.getName() + "\n");
        Stream.of(clazz.getDeclaredMethods())
                .forEach(method -> {
                            method.setAccessible(true);
                            System.out.println("Method name: " + method.getName());

                            System.out.println("Annotations: ");
                            boolean flag = true;
                            for (Annotation annotation : method.getAnnotations()) {
                                if (annotation.annotationType().getName().equals(RunMe.class.getName())) {
                                    methodsWithRunMe.add(method);
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                methodsWithOutRunMe.add(method);
                            }
                        }
                );

        writeOutputToFile(methodsWithRunMe, methodsWithOutRunMe);
    }

    public void writeOutputToFile(List<Method> methodsWithRunMe, List<Method> methodsWithOutRunMe) {
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
package org.app.material;

import org.app.material.annotation.Experimental;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

public class ExperimentalAnalyzer {

    public static void analyze() {
        try {
            for (Method method : ExperimentalAnalyzer.class
                    .getClassLoader()
                    .loadClass(("org.app.material.ExperimentalAnalyzer"))
                    .getMethods()) {
                // if annotation exist in the method
                if (method.isAnnotationPresent(org.app.material.annotation.Experimental.class)) {
                    try {
                        // all annotations run
                        for (Annotation annotation : method.getDeclaredAnnotations()) {
                            System.out.println("Аннотация в методе '" + method + "' : " + annotation);
                        }

                        Experimental annotation = method.getAnnotation(Experimental.class);

                        if (Objects.equals(annotation.priority(), "MEDIUM")) {
                            System.out.println("Method with priority = " + method);
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
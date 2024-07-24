package edu.caltech.cs2.helpers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestExtension implements TestExecutionExceptionHandler {
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_WHITE = "\u001B[37m";
    
    private final PrintStream outStream;
    
    public TestExtension(){
        this.outStream = System.out;
    }
    
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        DependsOn d = context.getTestMethod().get().getAnnotation(DependsOn.class);
        outStream.print(TEXT_YELLOW);
        outStream.println(context.getDisplayName());
        outStream.println("=".repeat(context.getDisplayName().length()));
        TestDescription t2 = context.getTestMethod().get().getAnnotation(TestDescription.class);
        if (t2 != null) {
            outStream.println(t2.value());
        }
        outStream.print(TEXT_PURPLE);
        if (d != null) {
            outStream.println("Hint: This test depends on the following being implemented correctly:\n    - " + String.join("\n    - ", d.value()));
        }
        TestHint t = context.getTestMethod().get().getAnnotation(TestHint.class);
        if (t != null) {
            outStream.println("Hint: " + t.value());
        }
        outStream.print(TEXT_RESET);
        throw throwable;
    }
}
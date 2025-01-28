package edu.kit.kastel;

public class LambdaMethods {

    // See methods foo (Line 27) and executeSomeCommand (Line 63) down below

    public static class Runnable {
        void run() { }
        void run2() { }
    }

    public static class RunnableClass {
        void run() { }
        void run2() { }
    }

    public void someMethodCall(Runnable r) {
        r.run();
        r.run2();
    }

    public void someMethodCall2(RunnableClass r) {
        r.run();
        r.run2();
    }

    public void foo() {
        someMethodCall(new Runnable() {
            @Override
            public void run() {
                // ...
            }
            @Override
            public void run2() {
                // ...
            }
        });

        someMethodCall2(new RunnableClass() {
            @Override
            public void run() {
                // ...
            }
            @Override
            public void run2() {
                // ...
            }
        });
    }

    public record CommandResult(String message) { }

    public interface CommandCallback {
        void run(CommandResult result);
    }

    public void execute(CommandCallback callback) {
        /* ggf. code, wir haben hier irgendwo unser CommandResult result */
        CommandResult result = new CommandResult("Command executed successfully");
        callback.run(result);
    }

    public void executeSomeCommand() {
        execute((result) -> System.out.println(result.message));
    }
}

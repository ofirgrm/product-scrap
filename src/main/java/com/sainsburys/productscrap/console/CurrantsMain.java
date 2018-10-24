package com.sainsburys.productscrap.console;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CurrantsMain {

    private String[] mainArgs;

    public static void main(String[] args) {
        System.out.println("Howdy Mates!");
        CurrantsMain main = new CurrantsMain();
        main.exec(args);
    }

    private void exec(String[] args) {
        if (args != null) {
            this.mainArgs = args;
        }

        System.out.println("Press 0 to exit");
        System.out.println("Press 1 to Start Spring Boot");
        System.out.println("Press 2 to Printout response");
        int result = readConsoleInput(0, 2);
        handleResult(result);
    }

    private void springBootDone() {
        System.out.println("Press 0 to exit");
        System.out.println("Press 1 to make HTTP call to get the results");
        System.out.println("Press 2 to return to main");
        int result = readConsoleInput(0, 2);
        handleResult(result + 3);
    }

    private void handleResult(int result) {
        switch (result) {
            case 0:
            case 3:
                System.out.println("Good Bye Y'all");
                System.exit(0);
            case 1:
                Future<Boolean> future = CurrantsServiceDirect.getInstance().springBoot(mainArgs);
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                springBootDone();
                break;
            case 2:
                System.out.println(CurrantsServiceDirect.getInstance().callDirectly());
                exec(null);
                break;
            case 4:
                System.out.println(CurrantsServiceDirect.getInstance().callHttpRequest());
                springBootDone();
                break;
            case 5:
                CurrantsServiceDirect.getInstance().springShutdown(mainArgs);
                exec(null);
                break;
        }
    }

    private int readConsoleInput(int from, int to) {
        do {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
                String token = scanner.next();
                if (token.length() == 1 && Character.isDigit(token.charAt(0))) {
                    int value = Integer.valueOf(token);
                    if (value >= from && value <= to) {
                        return value;
                    }
                }
            }
            System.out.println(String
                    .format("Please type a digit from: %d-%d following by Enter", from, to));
        }
        while (true);
    }

}

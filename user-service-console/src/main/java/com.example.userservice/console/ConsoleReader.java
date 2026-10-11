package com.example.userservice.console;

import java.util.Scanner;

public class ConsoleReader {
    private String[] args;
    private String command;
    private final Scanner scanner;
    private static final String ENTER_COMMAND = "Enter the command: add/get/list/update/delete/exit";

    public ConsoleReader() {
        this.scanner = new Scanner(System.in);
    }

    public void read() {
        System.out.println(ENTER_COMMAND);
        String input = scanner.nextLine();
        String[] parts = input.split("\\s+");
        this.command = parts[0].toLowerCase();
        if (parts.length > 1) {
            this.args = new String[parts.length - 1];
            System.arraycopy(parts, 1, this.args, 0, parts.length - 1);
        } else {
            this.args = new String[0];
        }
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommand() {
        return command;
    }
}
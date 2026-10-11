package com.example.userservice.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDispatcher {
    private static final Logger log = LoggerFactory.getLogger(CommandDispatcher.class);
    private final Map<String, Command> commands = new HashMap<>();
    private static final String ERROR_MESSAGE = "Error: ";
    private static final String UNKNOWN_COMMAND = "Unknown command";

    public CommandDispatcher(List<Command> listCommand) {
        for (Command command : listCommand) {
            commands.put(command.getName(), command);
        }
    }

    public void dispatch(String commandName, String[] args) {
        Command command = commands.get(commandName);
        if (command != null) {
            Result result;
            try {
                result = command.execute(args);
            } catch (HibernateException e) {
                log.error("Hibernate error", e);
                result = Result.failure("Database error: " + e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error", e);
                result = Result.failure("Unexpected error: " + e.getMessage());
            }
            if (result.isSuccess()) {
                System.out.println(result.getSuccessMessage());
            } else {
                System.out.println(ERROR_MESSAGE + result.getErrorMessage());
            }
        } else {
            System.out.println(UNKNOWN_COMMAND);
        }
    }
}
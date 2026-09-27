package com.example.userservice;

import com.example.userservice.commands.AddCommand;
import com.example.userservice.commands.Command;
import com.example.userservice.commands.CommandDispatcher;
import com.example.userservice.commands.DeleteCommand;
import com.example.userservice.commands.ExitCommand;
import com.example.userservice.commands.GetCommand;
import com.example.userservice.commands.ListCommand;
import com.example.userservice.commands.UpdateCommand;
import com.example.userservice.conveyor.ConveyorCommand;
import com.example.userservice.conveyor.ConveyorState;
import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.console.ConsoleReader;
import com.example.userservice.util.HibernateUtil;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        ConsoleReader reader = new ConsoleReader();
        ConveyorState state = new ConveyorState();
        List<Command> commands = List.of(
                new AddCommand(userDao),
                new GetCommand(userDao),
                new ListCommand(userDao),
                new UpdateCommand(userDao),
                new DeleteCommand(userDao),
                new ExitCommand(state)
        );
        CommandDispatcher dispatcher = new CommandDispatcher(commands);
        ConveyorCommand conveyor = new ConveyorCommand(reader, dispatcher, state);
        try {
            conveyor.startConveyor();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
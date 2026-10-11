package com.example.userservice.config;

import com.example.userservice.commands.AddCommand;
import com.example.userservice.commands.Command;
import com.example.userservice.commands.CommandDispatcher;
import com.example.userservice.commands.DeleteCommand;
import com.example.userservice.commands.ExitCommand;
import com.example.userservice.commands.GetCommand;
import com.example.userservice.commands.ListCommand;
import com.example.userservice.commands.UpdateCommand;
import com.example.userservice.console.ConsoleReader;
import com.example.userservice.conveyor.ConveyorCommand;
import com.example.userservice.conveyor.ConveyorState;
import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.service.UserService;
import com.example.userservice.service.UserServiceImpl;
import com.example.userservice.util.HibernateUtil;
import java.util.List;
import org.hibernate.SessionFactory;

public class AppConfig {
    public static ConveyorCommand buildApplication() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        UserDao userDao = new UserDaoImpl(sessionFactory);
        UserService userService = new UserServiceImpl(userDao);
        ConsoleReader reader = new ConsoleReader();
        ConveyorState state = new ConveyorState();
        List<Command> commands = List.of(
                new AddCommand(userService),
                new GetCommand(userService),
                new ListCommand(userService),
                new UpdateCommand(userService),
                new DeleteCommand(userService),
                new ExitCommand(state)
        );
        CommandDispatcher dispatcher = new CommandDispatcher(commands);
        return new ConveyorCommand(reader, dispatcher, state);
    }

    public static void shutdown() {
        HibernateUtil.shutdown();
    }
}
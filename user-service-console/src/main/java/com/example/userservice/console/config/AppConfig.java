package com.example.userservice.console.config;

import com.example.userservice.console.commands.AddCommand;
import com.example.userservice.console.commands.Command;
import com.example.userservice.console.commands.CommandDispatcher;
import com.example.userservice.console.commands.DeleteCommand;
import com.example.userservice.console.commands.ExitCommand;
import com.example.userservice.console.commands.GetCommand;
import com.example.userservice.console.commands.ListCommand;
import com.example.userservice.console.commands.UpdateCommand;
import com.example.userservice.console.console.ConsoleReader;
import com.example.userservice.console.conveyor.ConveyorCommand;
import com.example.userservice.console.conveyor.ConveyorState;
import com.example.userservice.console.dao.UserDao;
import com.example.userservice.console.dao.UserDaoImpl;
import com.example.userservice.common.service.UserService;
import com.example.userservice.console.service.UserServiceImpl;
import com.example.userservice.console.util.HibernateUtil;
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
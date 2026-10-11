package com.example.userservice.commands;

import com.example.userservice.conveyor.ConveyorState;

public class ExitCommand implements Command {
    private final ConveyorState conveyorState;
    private static final String EXIT = "EXITING...";

    public ExitCommand(ConveyorState conveyorState) {
        this.conveyorState = conveyorState;
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public Result execute(String[] args) {
        conveyorState.stop();
        return Result.success(EXIT);
    }
}
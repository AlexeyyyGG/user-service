package com.example.userservice.console;

import com.example.userservice.console.config.AppConfig;
import com.example.userservice.console.conveyor.ConveyorCommand;

public class Main {
    public static void main(String[] args) {
        ConveyorCommand conveyor = AppConfig.buildApplication();
        try {
            conveyor.startConveyor();
        } finally {
            AppConfig.shutdown();
        }
    }
}
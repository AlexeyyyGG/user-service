package com.example.userservice;

import com.example.userservice.config.AppConfig;
import com.example.userservice.conveyor.ConveyorCommand;

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
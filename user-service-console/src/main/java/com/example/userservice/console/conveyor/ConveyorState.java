package com.example.userservice.console.conveyor;

public class ConveyorState {
    private boolean running = true;

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        this.running = false;
    }
}
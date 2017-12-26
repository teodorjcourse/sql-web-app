package com.juja.webapp.teodor.controller.commands;

import java.util.HashMap;

public class CommandsRegistry<T> {
    private HashMap<String, Class<T>> commands;

    public CommandsRegistry(HashMap<String, Class<T>> commands) {
        this.commands = commands;
    }

    public Class<T> getCommandClass(String commandName) {
        return commands.get(commandName);
    }
}

package com.juja.webapp.teodor.controller.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommandsManager {
    private CommandsFactory<Command> factory;

    public CommandsManager(CommandsFactory<Command> factory) {
        this.factory = factory;
    }

    public void tryToExecute(String command, HttpServletRequest req, HttpServletResponse resp)
            throws Exception
    {
        Command cmd = factory.createCommand(command, req, resp);
        cmd.execute();
    }
}

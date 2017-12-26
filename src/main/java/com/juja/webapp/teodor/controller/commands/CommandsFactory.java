package com.juja.webapp.teodor.controller.commands;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class CommandsFactory<T> {
    private CommandsRegistry<T> commandsRegistry;

    public CommandsFactory(CommandsRegistry<T> commandsRegistry) {
        this.commandsRegistry = commandsRegistry;
    }

    public CommandsFactory(HashMap<String, Class<T>> commands) {
        this.commandsRegistry = new CommandsRegistry<>(commands);
    }

    public T createCommand(String command, Object... constructorParameters) throws Exception {
        Class<T> cl = commandsRegistry.getCommandClass(command);
        if (cl != null) {
            Constructor<?>[] cmdDeclaredCtors = cl.getDeclaredConstructors();
            for (int cmdCtorIndex = 0; cmdCtorIndex < cmdDeclaredCtors.length; cmdCtorIndex++) {
                Constructor<?> cmdDeclaredCtor = cmdDeclaredCtors[cmdCtorIndex];
                Class<?>[] parameterTypes = cmdDeclaredCtor.getParameterTypes();

                if (parameterTypes.length == constructorParameters.length) {
                    for (int cmdCtorParamIndex = 0; cmdCtorParamIndex < parameterTypes.length; cmdCtorParamIndex++) {
                        Class<?> parameterType = parameterTypes[cmdCtorParamIndex];

                        if (!parameterType.isInstance(constructorParameters[cmdCtorParamIndex])) {
                            break;
                        }
                    }
                    return cl.getConstructor(parameterTypes).newInstance(constructorParameters);
                }

            }

            throw new RuntimeException("Error while creating command.");
        } else {
            throw new RuntimeException("Command '" + command + "' doesn't supported.");
        }
    }
}

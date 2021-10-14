package server.commands;

import general.ExecutionResult;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {
    public final String name;
    public final String description;
    public final String pattern;

    protected AbstractCommand(String name, String description, String pattern) {
        this.name = name;
        this.description = description;
        this.pattern = pattern;
    }

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.pattern = name;
    }

    public String getName() {
        return name;
    }

    public String getFullDescription() {
        return pattern + " - " + description;
    }

    abstract ExecutionResult<?> execute(Object basicArgument, Object complexArgument);
}

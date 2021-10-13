package server.commands;

public abstract class Executable {
    public final String name;
    public final String description;
    public final String pattern;

    protected Executable(String name, String description, String pattern) {
        this.name = name;
        this.description = description;
        this.pattern = pattern;
    }

    public Executable(String name, String description) {
        this.name = name;
        this.description = description;
        this.pattern = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPattern() {
        return pattern;
    }

    abstract String execute(Object basicArgument, Object complexArgument);
}

package server.commands;

/**
 * Basic command.
 * Displays all available commands with descriptions given in their fields.
 *
 * @see CommandManager
 */
public class Help extends Executable {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "display list of all available commands");
        this.commandManager = commandManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        StringBuilder answer = new StringBuilder("List of all available commands:");

        for (Executable executable : commandManager.getCommandMap().values()) {
            answer.append("\n");
            answer.append(executable.getName());
            answer.append(" - ");
            answer.append(executable.getDescription());
        }

        return answer.toString();
    }
}

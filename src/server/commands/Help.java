package server.commands;

/**
 * Basic command.
 * Displays all available commands with descriptions given in their fields.
 *
 * @see CommandManager
 */
public class Help extends AbstractCommand {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "display list of all available commands");
        this.commandManager = commandManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        StringBuilder answer = new StringBuilder("List of all available commands:");

        for (AbstractCommand abstractCommand : commandManager.getCommandMap().values()) {
            answer.append("\n");
            answer.append(abstractCommand.getName());
            answer.append(" - ");
            answer.append(abstractCommand.getDescription());
        }

        return answer.toString();
    }
}

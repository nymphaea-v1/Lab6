package server.commands;

import general.ExecutionResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public ExecutionResult<String[]> execute(Object basicArgument, Object complexArgument) {
        List<String> abstractCommandsList = new ArrayList<>();
        for (AbstractCommand abstractCommand : commandManager.getCommandMap().values()) {
            abstractCommandsList.add(abstractCommand.getFullDescription());
        }
        String[] abstractCommands = abstractCommandsList.toArray(new String[0]);

        return new ThisExecutionResult(true, "List of all available commands:", abstractCommands);
    }

    private static class ThisExecutionResult extends ExecutionResult<String[]> {

        public ThisExecutionResult(boolean isSuccess, String description, String[] result) {
            super(isSuccess, description, result);
        }

        @Override
        public void printResult() {
            System.out.println(description);
            Arrays.stream(result).forEach(System.out::println);
        }
    }
}

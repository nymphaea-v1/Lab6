package server.commands;

import general.ExecutionResult;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.Arrays;
/**
 * Basic command with an argument.
 * Displays all the elements of the collection whose name starts with the given substring.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class FilterStartsWithName extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterStartsWithName(CollectionManager collectionManager) {
        super("filter_starts_with_name", "display all elements with a name starting with specified substring", "filter_starts_with_name name");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<Ticket[]> execute(Object basicArgument, Object complexArgument) {
        String name = (String) basicArgument;

        Ticket[] tickets = collectionManager.getValues().stream()
                .filter(ticket -> ticket.getName().startsWith(name))
                .toArray(Ticket[]::new);

        return new ThisExecutionResult(true, "Elements found: %d\n", tickets);
    }

    private static class ThisExecutionResult extends ExecutionResult<Ticket[]> {

        public ThisExecutionResult(boolean isSuccess, String description, Ticket[] result) {
            super(isSuccess, description, result);
        }

        @Override
        public void printResult() {
            System.out.printf(description, result.length);
            Arrays.stream(result).forEach(System.out::println);
        }
    }
}

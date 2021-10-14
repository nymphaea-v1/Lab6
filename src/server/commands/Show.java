package server.commands;

import general.ExecutionResult;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.*;

/**
 * Basic command.
 * Displays all elements of the collection and their total count.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Show extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "display this collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<HashMap<Long, Ticket>> execute(Object basicArgument, Object complexArgument) {
        int size = collectionManager.getSize();

        if (size == 0) return new ThisExecutionResult(true, "Collection is empty", null);

        return new ThisExecutionResult(true, null, collectionManager.getCollection());
    }

    private static class ThisExecutionResult extends ExecutionResult<HashMap<Long, Ticket>> {

        public ThisExecutionResult(boolean isSuccess, String description, HashMap<Long, Ticket> result) {
            super(isSuccess, description, result);
        }

        @Override
        public void printResult() {
            if (result == null) {
                System.out.println(description);
                return;
            }

            System.out.println("All elements of the collection:");
            result.entrySet().stream()
                    .sorted(Comparator.comparing(element -> element.getValue().getName()))
                    .forEach(element -> System.out.println(element.getKey() + ": " + element.getValue()));
            System.out.println("A total of " + result.size() + " elements");
        }
    }
}

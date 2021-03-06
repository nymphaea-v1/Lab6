package server.commands;

import general.ExecutionResult;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Complex command.
 * Removes all elements that greater than the given one.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class RemoveGreater extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager) {
        super("remove_greater", "remove all greater elements");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<Integer> execute(Object basicArgument, Object complexArgument) {
        Ticket ticket = (Ticket) complexArgument;

        int sizeBefore = collectionManager.getSize();

        collectionManager.getEntrySet().removeIf(element -> element.getValue().compareTo(ticket) > 0);

        return new ExecutionResult<>(true, "Elements removed: %d", sizeBefore - collectionManager.getSize());
    }
}

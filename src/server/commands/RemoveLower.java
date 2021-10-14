package server.commands;

import general.ExecutionResult;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Complex command.
 * Removes all elements that lower than the given one.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class RemoveLower extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveLower(CollectionManager collectionManager) {
        super("remove_lower", "remove all lower elements");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<Integer> execute(Object basicArgument, Object complexArgument) {
        Ticket ticket = (Ticket) complexArgument;

        int sizeBefore = collectionManager.getSize();

        collectionManager.getEntrySet().removeIf(element -> element.getValue().compareTo(ticket) < 0);

        return new ExecutionResult<>(true, "Elements removed: %d", sizeBefore - collectionManager.getSize());
    }
}

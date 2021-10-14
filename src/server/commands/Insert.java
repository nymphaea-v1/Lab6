package server.commands;

import general.ExecutionResult;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.Date;

/**
 * Complex command with an argument.
 * Inserts the new element with the specified key in the collection.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Insert  extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Insert(CollectionManager collectionManager) {
        super("insert", "insert new element with specified key", "insert key");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<Long> execute(Object basicArgument, Object complexArgument) {
        Long key = (Long) basicArgument;
        Ticket ticket = (Ticket) complexArgument;

        ticket.setExtraFields(collectionManager.getNextId(), new Date());

        if (collectionManager.containsKey(key)) return new ExecutionResult<>(false, "Element with key %d has already existing", key);

        collectionManager.setElement(key, ticket);
        return new ExecutionResult<>(true, "Ticket with key %d has been added to the collection", key);
    }
}

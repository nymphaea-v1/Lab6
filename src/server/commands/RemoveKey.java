package server.commands;

import general.ExecutionResult;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Basic command with an argument.
 * Allows removing one element from the collection by a given key.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class RemoveKey extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveKey(CollectionManager collectionManager) {
        super("remove_key", "remove element with specified key", "remove_key key");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<Long> execute(Object basicArgument, Object complexArgument) {
        Long key = (Long) basicArgument;

        if (!collectionManager.removeElement(key)) return new ExecutionResult<>(false, "No elements with key %d found", key);
        return new ExecutionResult<>(true, "Element with key %d has been removed", key);
    }
}

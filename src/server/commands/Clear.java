package server.commands;

import general.ExecutionResult;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Basic command.
 * Clears a collection managed by a specified collection manager.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Clear extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "clear this collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<String> execute(Object basicArgument, Object complexArgument) {
        collectionManager.clear();
        return new ExecutionResult<>(true, "Collection has been cleared");
    }
}

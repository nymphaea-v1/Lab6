package server.commands;

import server.CollectionManager;
import commands2.CommandManager;

/**
 * Basic command with an argument.
 * Allows removing from the collection all elements whose key is lower than the given one.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class RemoveLowerKey extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveLowerKey(CollectionManager collectionManager) {
        super("remove_lower_key", "remove all elements with a key lower than specified", "remove_lower_key key");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        Long key = (Long) basicArgument;

        int sizeBefore = collectionManager.getSize();

        collectionManager.getEntrySet().removeIf(element -> element.getKey() < key);
        return "Elements with key lower than " + key + " removed: " + (sizeBefore - collectionManager.getSize());
    }
}

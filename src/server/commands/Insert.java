package server.commands;

import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Complex command with an argument.
 * Inserts the new element with the specified key in the collection.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Insert  extends Executable {
    private final CollectionManager collectionManager;

    public Insert(CollectionManager collectionManager) {
        super("insert", "insert new element with specified key", "insert key");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        Long key = (Long) basicArgument;
        Ticket ticket = (Ticket) complexArgument;

        if (collectionManager.containsKey(key)) return "Element with key " + key + " has already existing";

        collectionManager.setElement(key, ticket);
        return "Ticket with key " + key + " has been added to the collection";
    }
}

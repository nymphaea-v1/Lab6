package server.commands;

import general.ticket.Ticket;
import general.ticket.TicketType;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Basic command with an argument.
 * Displays the number of elements with the given general.ticket type.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class CountByType extends AbstractCommand {
    private final CollectionManager collectionManager;

    public CountByType(CollectionManager collectionManager) {
        super("count_by_type", "display the number of elements with specified ticket type", "count_by_type type");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        TicketType type = (TicketType) basicArgument;

        int count = 0;
        for (Ticket ticket : collectionManager.getValues()) if (ticket.getType().equals(type)) count++;

        return "Elements found: " + count;
    }
}

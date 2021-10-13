package server.commands;

import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.Map;

/**
 * Complex command with an argument.
 * Updates an element of the collection with the specified id.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Update extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update", "update element with specified id", "update id");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        Long id = (Long) basicArgument;
        Ticket ticket = (Ticket) complexArgument;

        Map.Entry<Long, Ticket> ticketToUpdate = collectionManager.getValueById(id);

        if (ticketToUpdate == null) return "No elements with id " + id + " found";

        Ticket newTicket = new Ticket(ticket.getName(), ticket.getPrice(), ticket.getType(), ticket.getCoordinates(), ticket.getPerson(), ticketToUpdate.getValue().getId(), ticketToUpdate.getValue().getCreationDate());

        collectionManager.updateElement(ticketToUpdate.getKey(), newTicket);

        return "Element with id " + id + " has been updated";
    }
}

package server.commands;

import general.ticket.Person;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.Map;

/**
 * Complex command.
 * Removes all elements whose person is lower than the given one.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class RemoveAnyByPerson extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveAnyByPerson(CollectionManager collectionManager) {
        super("remove_any_by_person", "remove one element with specified person");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        Person person = (Person) complexArgument;

        for (Map.Entry<Long, Ticket> entry : collectionManager.getEntrySet()) {
            if (!(entry.getValue().getPerson().equals(person))) continue;

            collectionManager.removeElement(entry.getKey());

            return "One element with " + person + " has been removed";
        }

        return "There are no elements with " + person;
    }
}

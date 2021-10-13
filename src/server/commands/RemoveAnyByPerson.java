package server.commands;

import general.ticket.Person;
import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        int size = collectionManager.getSize();

        collectionManager.getEntrySet().removeIf(element -> collectionManager.getSize() == size && element.getValue().getPerson().equals(person));

//        List<Map.Entry<Long, Ticket>> elementsList = collectionManager.getEntrySet().stream()
//                .filter(element -> element.getValue().getPerson().equals(person))
//                .collect(Collectors.toList());

        if (collectionManager.getSize() == size) return "There are no elements with " + person;
        return "One element with " + person + " has been removed";
    }
}

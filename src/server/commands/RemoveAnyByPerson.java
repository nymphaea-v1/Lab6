package server.commands;

import general.ExecutionResult;
import general.ticket.Person;
import server.CollectionManager;
import commands2.CommandManager;

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
    public ExecutionResult<Person> execute(Object basicArgument, Object complexArgument) {
        Person person = (Person) complexArgument;
        int size = collectionManager.getSize();

        collectionManager.getEntrySet().removeIf(element -> collectionManager.getSize() == size && element.getValue().getPerson().equals(person));

        if (collectionManager.getSize() == size) return new ExecutionResult<>(false, "There are no elements with %s", person);
        return new ExecutionResult<>(true, "One element with %s has been removed", person);
    }
}

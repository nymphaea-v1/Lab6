package server.commands;

import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

/**
 * Basic command with an argument.
 * Displays all the elements of the collection whose name starts with the given substring.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class FilterStartsWithName extends Executable {
    private final CollectionManager collectionManager;

    public FilterStartsWithName(CollectionManager collectionManager) {
        super("filter_starts_with_name", "display all elements with a name starting with specified substring", "filter_starts_with_name name");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        String name = (String) basicArgument;

        StringBuilder answer = new StringBuilder("Elements found: ");
        int number = 0;

        for (Ticket ticket : collectionManager.getValues()) {
            if (ticket.getName().startsWith(name)) {
                answer.append(ticket.toString());
                number++;
            }
        }

        answer.append(number);

        return answer.toString();
    }
}

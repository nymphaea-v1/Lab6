package server.commands;

import general.ticket.Ticket;
import server.CollectionManager;
import commands2.CommandManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic command with an argument.
 * Displays all the elements of the collection whose name starts with the given substring.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class FilterStartsWithName extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterStartsWithName(CollectionManager collectionManager) {
        super("filter_starts_with_name", "display all elements with a name starting with specified substring", "filter_starts_with_name name");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        String name = (String) basicArgument;

        StringBuilder answer = new StringBuilder("Elements found: ");
        List<String> tickets = new ArrayList<>();

        collectionManager.getValues().stream()
                .filter(ticket -> ticket.getName().startsWith(name))
                .forEach(ticket -> tickets.add("\n" + ticket.toString()));

        answer.append(tickets.size());
        tickets.forEach(answer::append);

        return answer.toString();
    }
}

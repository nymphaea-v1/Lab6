package server.commands;

import general.ExecutionResult;
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
    public ExecutionResult<Long> execute(Object basicArgument, Object complexArgument) {
        TicketType type = (TicketType) basicArgument;

        long count = collectionManager.getValues().stream()
                .filter(element -> element.getType().equals(type))
                .count();

        return new ExecutionResult<>(true, "Elements found: %d", count);
    }
}

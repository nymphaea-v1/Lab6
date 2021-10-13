package server.commands;

import server.CollectionManager;
import commands2.CommandManager;

/**
 * Basic command.
 * Displays all elements of the collection and their total count.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Show extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "display this collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object basicArgument, Object complexArgument) {
        int size = collectionManager.getSize();

        if (size == 0) return "Collection is empty";

        String answer = "All elements of the collection:\n";
        answer += collectionManager.toString();
        answer += "A total of " + size + " elements";

        return answer;
    }
}

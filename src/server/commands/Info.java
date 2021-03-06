package server.commands;

import general.ExecutionResult;
import server.CollectionManager;
import commands2.CommandManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

/**
 * Basic command.
 * Displays creation and last modified time of the collection's file, the collection type and the number of elements.
 *
 * @see CommandManager
 * @see CollectionManager
 */
public class Info extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "display information about this collection");
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResult<String[]> execute(Object basicArgument, Object complexArgument) {
        String infoPattern = "Info about this collection:\nType: LinkedHashMap\nCreation date: %s\nLast modified date: %s\nNumber of elements: %s";
        Path filePath = collectionManager.getFilePath();

        String createTime = null;
        String updateTime = null;

        try {
            createTime = new Date(((FileTime) Files.getAttribute(filePath, "creationTime")).toMillis()).toString();
            updateTime = new Date(Files.getLastModifiedTime(filePath).toMillis()).toString();
        } catch (IOException | NullPointerException ignored) {}

        return new ExecutionResult<>(true, infoPattern, new String[] {createTime, updateTime, String.valueOf(collectionManager.getSize())});
    }
}

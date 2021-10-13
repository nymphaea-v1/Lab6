package server.commands;

import general.Command;
import general.exceptions.NoSuchCommandException;
import server.CollectionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a manager that creates a set of commands connected to the specified collection
 * and allows user to manage it with given commands.
 */
public class CommandManager {
    private final Map<String, Executable> commandMap;

    public CommandManager(CollectionManager collectionManager) {
        commandMap = new HashMap<>();

        commandMap.put("help", new Help(this));
        commandMap.put("info", new Info(collectionManager));
        commandMap.put("show", new Show(collectionManager));
        commandMap.put("clear", new Clear(collectionManager));

        commandMap.put("remove_key", new RemoveKey(collectionManager));
        commandMap.put("remove_lower_key", new RemoveLowerKey(collectionManager));
        commandMap.put("count_by_type", new CountByType(collectionManager));
        commandMap.put("filter_starts_with_name", new FilterStartsWithName(collectionManager));

        commandMap.put("remove_lower", new RemoveLower(collectionManager));
        commandMap.put("remove_greater", new RemoveGreater(collectionManager));
        commandMap.put("remove_any_by_person", new RemoveAnyByPerson(collectionManager));

        commandMap.put("update", new Update(collectionManager));
        commandMap.put("insert", new Insert(collectionManager));
    }

    public Map<String, Executable> getCommandMap() {
        return commandMap;
    }

    /**
     * Executes the specified command.z
     *
     * @param command command to be executed
     * @return results of the execution
     * @throws NoSuchCommandException thrown if there is no command with the specified name
     */
    public String execute(Command command) throws NoSuchCommandException {
        Executable executable =  commandMap.get(command.name);

        if (executable == null) throw new NoSuchCommandException();

        return executable.execute(command.basicArgument, command.complexArgument);
    }
}

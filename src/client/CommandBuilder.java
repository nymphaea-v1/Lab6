package client;

import general.Command;
import general.TicketReader;
import general.exceptions.IncorrectArgumentException;
import general.exceptions.NoSuchCommandException;
import general.ticket.TicketType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandBuilder {
    private final static List<String> simpleCommandList = Arrays.asList("help", "info", "show", "clear");
    private final static Map<String, GetBasicArgument> basicArgumentCommand = new HashMap<>();
    private final static Map<String, GetComplexArgument> complexArgumentCommand = new HashMap<>();

    static {
        basicArgumentCommand.put("remove_key", Long::parseLong);
        basicArgumentCommand.put("remove_lower_key", Long::parseLong);
        basicArgumentCommand.put("count_by_type", TicketType::valueOf);
        basicArgumentCommand.put("filter_starts_with_name", null);
        basicArgumentCommand.put("update", Long::parseLong);
        basicArgumentCommand.put("insert", Long::parseLong);

        complexArgumentCommand.put("remove_greater", TicketReader::readTicket);
        complexArgumentCommand.put("remove_lower", TicketReader::readTicket);
        complexArgumentCommand.put("remove_any_by_person", TicketReader::readPerson);
        complexArgumentCommand.put("update", TicketReader::readTicket);
        complexArgumentCommand.put("insert", TicketReader::readTicket);
    }

    public static Command getCommand(InputReader inputReader, String name, String basicArgumentString) throws NoSuchCommandException, IncorrectArgumentException {
        if (simpleCommandList.contains(name)) return new Command(name, null, null);
        if (!basicArgumentCommand.containsKey(name) && !complexArgumentCommand.containsKey(name)) throw new NoSuchCommandException();

        GetBasicArgument getBasicArgument = basicArgumentCommand.get(name);
        GetComplexArgument getComplexArgument = complexArgumentCommand.get(name);

        Serializable basicArgument = null;
        Serializable complexArgument = null;

        if (basicArgumentCommand.containsKey(name)) {
            if (basicArgumentString == null) throw new IncorrectArgumentException("no argument");

            if (getBasicArgument != null) {
                try {
                    basicArgument = getBasicArgument.get(basicArgumentString);
                } catch (IllegalArgumentException e) {
                    throw new IncorrectArgumentException(basicArgumentString);
                }
            } else basicArgument = basicArgumentString;
        }
        if (complexArgumentCommand.containsKey(name) && getComplexArgument != null) complexArgument = getComplexArgument.get(inputReader);

        return new Command(name, basicArgument, complexArgument);
    }

    private interface GetBasicArgument {
        Serializable get(String argument);
    }

    private interface GetComplexArgument {
        Serializable get(InputReader inputReader);
    }
}

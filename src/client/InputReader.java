package client;

import general.Command;
import general.Reader;
import general.exceptions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Simple class that is used to read commands and their arguments from the console or the specified files.
 * Uses a list of scanners to manage its input source.
 *
 * @see CommandBuilder
 */
public class InputReader {
    private final LinkedList<Scanner> scanners;
    private final LinkedList<String> filePaths;
    public boolean fromConsole;

    public InputReader() {
        scanners = new LinkedList<>();
        filePaths = new LinkedList<>();
        fromConsole = true;

        scanners.add(new Scanner(System.in).useDelimiter("\n"));
    }

    /**
     * Starts reading input from console and processing it to command manager
     */
    public void startReading(ServerConnector serverConnector) throws IOException {
        Scanner consoleScanner = new Scanner(System.in).useDelimiter("\n");
        scanners.add(consoleScanner);

        while (!scanners.isEmpty()) {
            Scanner scanner = scanners.getLast();

            if (!(fromConsole || scanner.hasNext())) {
                removeLastScanner();
                continue;
            }

            String next;
            try {
                next = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                stopReading();
                return;
            }

            if (next.isEmpty()) continue;

            String commandString;
            String argument = null;

            int spaceIndex = next.indexOf(" ");
            if (spaceIndex != -1) {
                commandString = next.substring(0, spaceIndex).toLowerCase();
                argument = next.substring(spaceIndex + 1);
            } else commandString = next.toLowerCase();

            if (exit(commandString)) return;

            Command command = null;

            do {
                try {
                    if (executeScript(commandString, argument)) break;

                    command = CommandBuilder.getCommand(this, commandString, argument);
                } catch (IncorrectArgumentException | NoSuchCommandException | CancelCommandException e) {
                    System.out.println(commandString + ": " + e.getMessage());

                    if (!fromConsole) toConsole();
                    else if (e instanceof NoSuchCommandException) System.out.println("Type help to get a list of all available commands");
                    else if (e instanceof IncorrectArgumentException) {
                        System.out.println("Try again or type 2? to cancel: ");

                        try {
                            argument = scanner.nextLine();
                        }  catch (NoSuchElementException ei) {
                            stopReading();
                            return;
                        }

                        if (!argument.equals("2?")) continue;

                        System.out.println("Current command execution has been cancelled");
                    }
                }

                break;
            } while (true);

            if (command == null) continue;

            do {
                try {
                    serverConnector.sendMessage(1, command);
                    break;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    serverConnector.connect();
                }
            } while (true);
        }
    }

    private boolean exit(String command) {
        if (!command.equals("exit")) return false;

        stopReading();
        return true;
    }

    private boolean executeScript(String command, String argument) throws IncorrectArgumentException {
        if (!command.equals("execute_script")) return false;

        try {
            return addToScan(argument);
        } catch (FileNotFoundException e) {
            throw new IncorrectArgumentException(argument);
        }
    }

    /**
     * Stops reading input. Clears its list of scanners.
     */
    public void stopReading() {
        scanners.clear();
        filePaths.clear();

        fromConsole = true;
    }

    /**
     * Adds a script file to read commands from
     *
     * @param path a path name to the script file
     * @throws FileNotFoundException if source is not found
     */
    public boolean addToScan(String path) throws FileNotFoundException {
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        if (filePaths.contains(absolutePath)) {
            System.out.println("Recursion");

            toConsole();
            return false;
        }

        Scanner scanner = new Scanner(file).useDelimiter("\\r?\\n|\\r");

        scanners.addLast(scanner);
        filePaths.addLast(absolutePath);

        fromConsole = false;
        return true;
    }

    /**
     * Removes the last scanner from its list
     */
    public void removeLastScanner() {
        scanners.removeLast().close();
        filePaths.removeLast();

        if (filePaths.isEmpty()) fromConsole = true;
    }

    /**
     * Goes back to reading input from the console.
     * Removes all scanners except the last one, which is the console scanner.
     */
    private void toConsole() {
        Scanner scanner = scanners.getFirst();

        scanners.clear();
        filePaths.clear();

        scanners.add(scanner);

        fromConsole = true;
    }

    /**
     * Reads an object from the last scanner.
     * This method is used by commands from command manager to read a complex argument.
     *
     * @param readers a list of readers performing operations with the last scanner
     * @return a list of fields that can be used to construct an object
     */
    public List<Object> readObject(List<Reader> readers) {
        return fromConsole ? readObjectConsole(readers) : readObjectFile(readers);
    }

    private List<Object> readObjectFile(List<Reader> readers) {
        List<Object> result = new ArrayList<>();
        Scanner scanner = scanners.getLast();

        for (Reader reader : readers) {
            if (!scanner.hasNext()) throw new CancelCommandException("end of file");

            try {
                result.add(reader.reader.read(scanner));
            } catch (IncorrectFieldException e) {
                throw new CancelCommandException(e.getMessage());
            }
        }

        return result;
    }

    private List<Object> readObjectConsole(List<Reader> readers) {
        List<Object> result = new ArrayList<>();
        Scanner scanner = scanners.getFirst();

        for (Reader reader : readers) {
            Object field;

            while (true) {
                try {
                    System.out.println("Enter " + reader.name + ":");

                    field = reader.reader.read(scanner);
                    if (field != null && field.toString().trim().equals("2?")) throw new CancelCommandException();

                    break;
                } catch (IncorrectFieldException e) {
                    String message = e.getMessage().trim();
                    if (message.equals("2?")) throw new CancelCommandException();
                    System.out.println("Invalid input (" + message + "). Try again:");
                } catch (NoSuchElementException e) {
                    throw new CancelCommandException();
                }
            }

            result.add(field);
        }

        return result;
    }
}

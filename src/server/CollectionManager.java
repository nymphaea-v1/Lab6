package server;

import general.ticket.Ticket;
import general.CSVParser.CSVParserButBetter;
import general.exceptions.IncorrectFieldException;
import general.TicketReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class represents a collection and provides the ability to edit and manage it.
 * Collection stored by this class is LinkedHashMap with Long key and Ticket value.
 *
 * @see LinkedHashMap
 * @see Ticket
 */
public class CollectionManager {
    private final LinkedHashMap<Long, Ticket> collection = new LinkedHashMap<>();
    private Path filePath;
    private long nextId;

    /**
     * Loads a existing collection from a CSV file.
     * If the file does not exist or an I/O error occurs, the collection will remain empty.
     *
     * @param filePathString path to the file
     */
    public CollectionManager(String filePathString) {
        try {
            filePath = Paths.get(filePathString);
            nextId = 0;

            addFromFile(filePath);
        } catch (IOException e) {
            System.out.println("Specified file's access error: " + e.getMessage());
            return;
        }

        if (collection.size() != 0) {
            sortByName();
            System.out.printf("Collection with %d elements has been initialized%n", getSize());
        } else System.out.println("This file does not contain any valid elements");
    }

    /**
     * Returns the file path represent the source of this collection
     *
     * @return the file path
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Returns an unique id that is not presented in the current collection.
     * Basically, user can set any id to new instance.
     * This method allows creating an instance specifically for this collection.
     *
     * @return an unique id
     */
    public long getNextId() {
        return nextId;
    }

    /**
     * Returns a current size of this collection
     *
     * @return int size
     */
    public int getSize() {
        return collection.size();
    }

    /**
     * Founds an element with specified id and returns it
     *
     * @param id general.ticket id
     * @return general.ticket if there is an element with the specified id, otherwise null
     */
    public Map.Entry<Long, Ticket> getValueById(long id) {
        for (Map.Entry<Long, Ticket> element : collection.entrySet()) {
            if (element.getValue().getId() == id) return element;
        }

        return null;
    }

    /**
     * Can be used to perform a search through the collection.
     *
     * @return set of all Map.Entry of this collection
     */
    public Set<Map.Entry<Long, Ticket>> getEntrySet() {
        return collection.entrySet();
    }

    public LinkedHashMap<Long, Ticket> getCollection() {
        return collection;
    }

    /**
     * Can be used to perform a search through the collection.
     *
     * @return set of all tickets of this collection
     */
    public Collection<Ticket> getValues() {
        return collection.values();
    }

    /**
     * Checks if this collection contains a mapping for the specified key.
     *
     * @param key the key whose presence in this map is to be tested
     * @return true if there is an entry with the specified key, otherwise false
     */
    public boolean containsKey(Long key) {
        return collection.containsKey(key);
    }

    /**
     * Sets the specified value with the specified key in this collection.
     * If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param ticket value to be associated with the specified key
     * @return false if the value cannot be set due to repeating id, otherwise true
     */
    public boolean setElement(Long key, Ticket ticket) {
        long id = ticket.getId();

        if (getValueById(id) != null) return false;

        collection.put(key, ticket);
        if (id >= nextId) nextId = id + 1;
        return true;
    }

    public void updateElement(Long key, Ticket ticket) {
        if (!collection.containsKey(key)) return;

        collection.put(key, ticket);
    }

    /**
     * Removes the mapping for the specified key from this collection if present.
     *
     * @param key key whose mapping is to be removed from the collection
     * @return true if the mapping was removed, otherwise false
     */
    public boolean removeElement(Long key) {
        return collection.remove(key) != null;
    }

    /**
     * Removes all of the elements from this collection.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Saves the entire collection to its source file
     *
     * @throws IOException if the file does not exist or an I/O error occurs
     */
    public void save() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath.toFile());
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

        for (Map.Entry<Long, Ticket> element : collection.entrySet()) {
            outputStreamWriter.write(convertToCSV(element) + "\n");
        }

        outputStreamWriter.close();
    }

    private void addFromFile(Path filePath) throws IOException {
        CSVParserButBetter parser = new CSVParserButBetter(filePath);
        while (parser.hasNext()) {
            try {
                Long key = Long.parseLong(parser.next());
                Ticket ticket = TicketReader.readTicket(parser);
                if (!setElement(key, ticket)) {
                    System.out.println("Elements with the same id were found, the last one was skipped");
                }
            } catch (IncorrectFieldException | CSVParserButBetter.CSVParsingException | NumberFormatException e) {
                if (parser.hasNext() && !parser.lineSkip) parser.skipLine();
                System.out.println("Object initialization failed: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("Object initialization failed: the end of file");
            }
        }

        parser.close();
    }

    private static String convertToCSV(Map.Entry<Long, Ticket> element) {
        return element.getKey() + ", " + element.getValue().toCSV();
    }

    public void sortByName() {
        List<Map.Entry<Long, Ticket>> elementsList = new ArrayList<>(collection.entrySet());

        collection.clear();

        elementsList.stream()
                .sorted(Comparator.comparing(element -> element.getValue().getName()))
                .forEach(element -> setElement(element.getKey(), element.getValue()));
    }
}

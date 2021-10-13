package general.CSVParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Simple CSV parser, implementation of the Iterator interface.
 * Based on a scanner, which parses specified file.
 *
 * @author pepejka
 */

public class CSVParserButBetter implements Iterator<String> {
    private final Scanner scanner;
    public boolean lineSkip = false;
    private static final String lineSeparator = "\\r?\\n|\\r";

    /**
     * @param source the path to the CSV file to be parsed
     * @throws IOException if the file does not exist or an I/O error occurs
     */

    public CSVParserButBetter(Path source) throws IOException {
        scanner = new Scanner(source).useDelimiter("");
    }

    /**
     *
     * @return true if and only if this parser has another value to be parsed
     */

    @Override
    public boolean hasNext() {
        while (scanner.hasNext("\\s")) {
            String next = scanner.next();
            if (!lineSkip && next.equals(lineSeparator)) lineSkip = true;
        }

        return scanner.hasNext();
    }

    /**
     * Finds and returns the next parsed value from the scanner inside.
     *
     * @return the next parsed value
     * @throws CSVParsingException if next value doesn't match the CSV format
     */

    @Override
    public String next() {
        StringBuilder resultBuilder = new StringBuilder();
        String next = scanner.skip("\\s*").next();

        // check if the element is not surrounded by quotation marks
        // if so, read an entire element until the end (comma, line separator or the end of file) and return it
        if (!next.equals("\"")) {
            while (!next.equals(",") && !next.matches(lineSeparator)) {
                if (next.equals("\"")) throw new CSVParsingException(("quotation mark inside an unquoted element"));
                resultBuilder.append(next);

                if (!scanner.hasNext()) return resultBuilder.toString().trim();
                next = scanner.next();
            }

            lineSkip = next.matches(lineSeparator);
            return resultBuilder.toString().trim();
        }

        while (true) {
            // go to the next symbol (skip starting quotation mark)
            if (!scanner.hasNext()) throw new CSVParsingException("no ending quotation mark");
            next = scanner.next();

            // check if the next symbol is not a quotation mark
            // if so, add it to result and go to the next symbol
            if (!next.equals("\"")) {
                resultBuilder.append(next);
                continue;
            }

            // check if quotation mark is inside an element
            // if so, add it to result and go to the next symbol
            if (!scanner.hasNext()) return resultBuilder.toString().trim();
            if (scanner.hasNext("\"")) {
                resultBuilder.append(scanner.next());
                continue;
            }

            // skip any left symbols to the end
            next = scanner.skip("\\s*").next();
            if (!next.equals(",") && !next.matches(lineSeparator)) throw new CSVParsingException("symbol after ending quotation mark");

            lineSkip = next.matches(lineSeparator);
            return resultBuilder.toString().trim();
        }
    }

    /**
     * Skips the next line of the scanner inside this parser.
     */

    public void skipLine() {
        scanner.nextLine();
        lineSkip = true;
    }

    /**
     *  Closes the scanner inside this parser.
     *  If this parser is already closed then invoking this method will have no effect.
     *  Attempting to perform search operations after a parser has been closed will result in an IllegalStateException.
     */

    public void close() {
        scanner.close();
    }

    public static class CSVParsingException extends RuntimeException {
        CSVParsingException(String message) {
            super(message);
        }
    }
}


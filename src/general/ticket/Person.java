package general.ticket;

import general.exceptions.IncorrectFieldException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.Objects;

public class Person implements Serializable {
    private final LocalDate birthday;
    private final double height;
    private final int weight;
    private final String passportID;

    public Person(LocalDate birthday, double height, int weight, String passportID) {
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
    }

    public String getPassportID() {
        return passportID;
    }

    public static LocalDate readBirthday(Iterator<String> iterator) throws IncorrectFieldException {
        String birthdayString = iterator.next().trim();

        try {
            return LocalDate.parse(birthdayString);
        } catch (DateTimeParseException e) {
            throw new IncorrectFieldException(birthdayString);
        }
    }

    public static double readHeight(Iterator<String> iterator) throws IncorrectFieldException {
        String heightString = iterator.next().trim();
        double height;

        try {
            height = Double.parseDouble(heightString);
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(heightString);
        }

        if (height <= 0) throw new IncorrectFieldException(heightString);

        return height;
    }

    public static int readWeight(Iterator<String> iterator) throws IncorrectFieldException {
        String weightString = iterator.next().trim();
        int weight;

        try {
            weight = Integer.parseInt(weightString);
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(weightString);
        }

        if (weight <= 0) throw new IncorrectFieldException(weightString);

        return weight;
    }

    public static String readPassportID(Iterator<String> iterator) throws IncorrectFieldException {
        String passportID = iterator.next().trim();

        if (passportID.length() == 0 || passportID.equals("null")) return null;
        if (passportID.length() < 10) throw new IncorrectFieldException(passportID);

        return passportID;
    }

    public String toCSV() {
        String passportID = this.passportID;
        if (passportID != null && (passportID.contains("\"") || passportID.contains(","))) {
            passportID = "\"" + passportID.replaceAll("\"", "\"\"") + "\"";
        }

        return birthday + ", " + height + ", " + weight + ", " + passportID;
    }

    @Override
    public String toString() {
        return "Person{ " +
                "birthday: " + birthday + ", " +
                "height: " + height + ", " +
                "weight: " + weight + ", " +
                "passportID: " + passportID + "}";
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Person)) return false;

        Person person = (Person) object;

        boolean equalPassportID = passportID != null && passportID.equals(person.getPassportID());
        return equalPassportID || (birthday.equals(person.birthday) && (height == person.height) && (weight == person.weight));
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthday, height, weight, passportID);
    }
}

package general.ticket;

import general.exceptions.IncorrectFieldException;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class Ticket implements Serializable, Comparable<Ticket> {
    private final transient long id;
    private final transient Date creationDate;

    private String name;
    private int price;
    private TicketType type;
    private Coordinates coordinates;
    private Person person;

    public Ticket(String name, int price, TicketType type, Coordinates coordinates, Person person, long id, Date creationDate) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.coordinates = coordinates;
        this.person = person;
        this.id = id;
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public TicketType getType() {
        return type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Person getPerson() {
        return person;
    }

    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public static long readId(Iterator<String> iterator) throws IncorrectFieldException {
        String idString = iterator.next().trim();

        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(idString);
        }
    }

    public static Date readCreationDate(Iterator<String> iterator) throws IncorrectFieldException {
        String creationDateString = iterator.next().trim();

        try {
            return new Date(Long.parseLong(creationDateString));
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(creationDateString);
        }
    }

    public static String readName(Iterator<String> iterator) throws IncorrectFieldException {
        String name = iterator.next().trim();

        if (name.isEmpty()) throw new IncorrectFieldException(name);

        return name;
    }

    public static Integer readPrice(Iterator<String> iterator) throws IncorrectFieldException {
        String priceString = iterator.next().trim();
        int price;

        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(priceString);
        }

        if (price <= 0) throw new IncorrectFieldException(priceString);

        return price;
    }

    public static TicketType readType(Iterator<String> iterator) throws IncorrectFieldException {
        String typeString = iterator.next().trim();

        try {
            return TicketType.valueOf(typeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IncorrectFieldException(typeString);
        }
    }

    public String toCSV() {
        String name = this.name;
        if (name != null &&(name.contains("\"") || name.contains(","))) {
            name = "\"" + name.replaceAll("\"", "\"\"") + "\"";
        }

        return name + ", " + price + ", " + type + ", "  + coordinates + ", " + person.toCSV() + ", " + id + ", " + creationDate.getTime();
    }

    @Override
    public String toString() {
        return "Ticket{ " +
                "id: " + id +
                ", name: " + name +
                ", coordinates: " + coordinates.getX() + ";" + coordinates.getY() +
                ", creationDate: " + creationDate +
                ", price: " + price +
                ", type: " + type +
                ", person: " + person.toString() + " }";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Ticket ticket = (Ticket) object;

        return name.equals(ticket.name)
                && price == ticket.price
                && type.equals(ticket.type)
                && coordinates.equals(ticket.coordinates)
                && person.equals(ticket.person)
                && id == ticket.id
                && creationDate.equals(ticket.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, type, coordinates, person, id, creationDate);
    }

    @Override
    public int compareTo(Ticket ticket) {
        if (ticket.equals(this)) return 0;

        int result = ticket.type.compareTo(type);
        if (result != 0) return result;

        result = Integer.compare(price, ticket.price);
        if (result != 0) return result;

        return ticket.name.compareTo(name);
    }
}

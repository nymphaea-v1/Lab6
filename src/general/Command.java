package general;

import java.io.Serializable;

public class Command implements Serializable {
    public final String name;
    public final Serializable basicArgument;
    public final Serializable complexArgument;

    public Command(String name, Serializable basicArgument, Serializable complexArgument) {
        this.name = name;
        this.basicArgument = basicArgument;
        this.complexArgument = complexArgument;
    }
}

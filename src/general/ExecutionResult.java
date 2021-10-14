package general;

import java.io.Serializable;

public class ExecutionResult<T extends Serializable> implements Serializable {
    public final boolean isSuccess;
    public final String description;
    public final T result;

    public ExecutionResult(boolean isSuccess, String description, T result) {
        this.isSuccess = isSuccess;
        this.description = description;
        this.result = result;
    }

    public ExecutionResult(boolean isSuccess, String description) {
        this.isSuccess = isSuccess;
        this.description = description;
        this.result = null;
    }

    public void printResult() {
        if (result == null) {
            System.out.println(description);
            return;
        }

        if (result instanceof Object[]) {
            System.out.printf(description + "\n", (Object[]) result);
            return;
        }

        System.out.printf(description + "\n", result);
    }
}

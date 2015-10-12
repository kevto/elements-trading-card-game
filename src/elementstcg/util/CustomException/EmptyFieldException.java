package elementstcg.util.CustomException;

public class EmptyFieldException extends Exception {

    public EmptyFieldException (String message) {
        super(message);
    }

    public EmptyFieldException (String message, Throwable throwable) {
        super(message, throwable);
    }

}

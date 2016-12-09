/**
 * StackEmptyException.java
 *
 * Write an Exception class, called StackEmptyException.
 * Your exception class should be a subclass of the RuntimeException class.
 */
public class StackEmptyException extends RuntimeException{
    public StackEmptyException() {
        super();
    }

    public StackEmptyException(String s) {
        super(s);
    }

    public StackEmptyException(String s, Throwable cause) {
        super(s, cause);
    }

    public StackEmptyException(Throwable cause) {
        super(cause);
    }


}
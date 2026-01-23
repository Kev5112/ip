public class EmptyTaskException extends RuntimeException {
    public EmptyTaskException(String message) {
        super(CommonWords.LINE + message + '\n' + CommonWords.LINE);
    }
}

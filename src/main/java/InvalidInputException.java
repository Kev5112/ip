public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(CommonWords.LINE + message + '\n' + CommonWords.LINE);
    }
}

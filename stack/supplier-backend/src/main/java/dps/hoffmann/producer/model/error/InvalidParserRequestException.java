package dps.hoffmann.producer.model.error;

public class InvalidParserRequestException extends RuntimeException {
    public InvalidParserRequestException(String message) {
        super(message);
    }
}

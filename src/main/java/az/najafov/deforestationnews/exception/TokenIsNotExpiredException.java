package az.najafov.deforestationnews.exception;

public class TokenIsNotExpiredException extends RuntimeException {

    public TokenIsNotExpiredException(String message) {
        super(message);
    }

}

package az.najafov.deforestationnews.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("Username with : " + username + " already exists");
    }

}

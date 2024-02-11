package az.najafov.deforestationnews.exception;

public class UserHasAlreadyReactionException extends RuntimeException {

    public UserHasAlreadyReactionException(Long userId, Long newsId) {
        super("User with id : " + userId + " has already same type reaction on news id : " + newsId);
    }

}

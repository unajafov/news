package az.najafov.deforestationnews.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class entity, String message) {
        super(entity.getSimpleName() + " " + message);
    }

    public EntityNotFoundException(Class entity, Object id) {
        super(entity.getSimpleName() + " with ID : " + id.toString());
    }

}
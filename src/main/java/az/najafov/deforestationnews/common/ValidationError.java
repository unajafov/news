package az.najafov.deforestationnews.common;

public class ValidationError {

    private String field;
    private String message;

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public ValidationError(String string) {
    }

}
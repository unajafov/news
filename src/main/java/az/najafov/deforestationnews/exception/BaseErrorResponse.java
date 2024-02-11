package az.najafov.deforestationnews.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseErrorResponse {

    private String message;
    private String code;
    private String reason;

}

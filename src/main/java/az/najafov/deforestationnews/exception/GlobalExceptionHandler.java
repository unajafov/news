package az.najafov.deforestationnews.exception;

import az.najafov.deforestationnews.common.ErrorMessageCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(400).body(new BaseErrorResponse("failure",
                ErrorMessageCode.ENTITY_NOT_FOUND.name(), ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(400).body(new BaseErrorResponse("failure",
                ErrorMessageCode.USERNAME_NOT_FOUND.name(), ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<BaseErrorResponse> handlePasswordMismatchException(PasswordMismatchException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(400).body(new BaseErrorResponse("failure",
                ErrorMessageCode.PASSWORD_MISMATCH.name(), ex.getMessage()));
    }

}

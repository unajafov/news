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

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<BaseErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(400).body(new BaseErrorResponse("failure",
                ErrorMessageCode.USERNAME_ALREADY_EXISTS.name(), ex.getMessage()));
    }

    @ExceptionHandler(LongTimeInActiveUserException.class)
    public ResponseEntity<BaseErrorResponse> handleLongTimeInActiveUserException(LongTimeInActiveUserException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(440).body(new BaseErrorResponse("failure",
                ErrorMessageCode.LONG_TIME_INACTIVE_USER.name(), ex.getMessage()));
    }

    @ExceptionHandler(TokenIsNotExpiredException.class)
    public ResponseEntity<BaseErrorResponse> handleTokenIsNotExpiredException(TokenIsNotExpiredException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(440).body(new BaseErrorResponse("failure",
                ErrorMessageCode.TOKEN_IS_NOT_EXPIRED.name(), ex.getMessage()));
    }

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleTokenNotValidException(TokenNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(440).body(new BaseErrorResponse("failure",
                ErrorMessageCode.TOKEN_NOT_VALID.name(), ex.getMessage()));
    }


}

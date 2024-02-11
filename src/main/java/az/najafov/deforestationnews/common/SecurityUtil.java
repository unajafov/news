package az.najafov.deforestationnews.common;

import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    public User getUser() {
        return getGeneralUser();
    }


    private User getGeneralUser() {
        var context = SecurityContextHolder.getContext();
        if (Objects.nonNull(context)) {
            var authentication = context.getAuthentication();
            if (Objects.nonNull(authentication)) {
                UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                if (Objects.nonNull(principal)) {
                    String username = principal.getUsername();
                    return userRepository.findByUsername(username)
                            .orElseThrow(() -> new EntityNotFoundException(User.class, username));
                }
            }
        }
        throw new RuntimeException();
    }

}

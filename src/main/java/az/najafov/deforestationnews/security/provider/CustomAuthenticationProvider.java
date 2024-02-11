package az.najafov.deforestationnews.security.provider;


import az.najafov.deforestationnews.dto.AuthenticationRequestDto;
import az.najafov.deforestationnews.security.userdetails.CustomUserDetailsService;
import az.najafov.deforestationnews.security.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final AuthenticationRequestDto authenticationRequestDto = (AuthenticationRequestDto) authentication.getPrincipal();
        String username = authenticationRequestDto.getUsername();
        UserDetails user = customUserDetailsService.loadUserByUsername(username);
        return createSuccessfulAuthentication(authentication, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication,
                                                          final UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), user.getAuthorities());
        final String rawPassword = (String) authentication.getCredentials();
        boolean isAuthenticated = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!isAuthenticated) {
            throw new BadCredentialsException("Password is incorrect!");
        }
        token.setDetails(authentication.getDetails());
        return token;
    }

}

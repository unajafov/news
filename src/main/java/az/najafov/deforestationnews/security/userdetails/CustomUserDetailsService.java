package az.najafov.deforestationnews.security.userdetails;

import az.najafov.deforestationnews.model.Role;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.userdetails.UserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(username));
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        Set<Role> roleSet = user.getRoles();
        for (Role role : roleSet) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new UserDetails(username, user.getPassword(), roles);
    }

}

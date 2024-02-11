package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.dto.AuthenticationRequestDto;
import az.najafov.deforestationnews.dto.LoginRequestDto;
import az.najafov.deforestationnews.dto.RegistrationRequestDto;
import az.najafov.deforestationnews.dto.TokenResponseDto;
import az.najafov.deforestationnews.exception.EntityNotFoundException;
import az.najafov.deforestationnews.exception.PasswordMismatchException;
import az.najafov.deforestationnews.exception.TokenNotValidException;
import az.najafov.deforestationnews.exception.UsernameAlreadyExistsException;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.model.Role;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.repository.RegionRepository;
import az.najafov.deforestationnews.repository.RoleRepository;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.provider.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RegionRepository regionRepository;
    private final RoleRepository roleRepository;

    @Value("${static.role.user_id}")
    private Long userRoleId;

    private static final Long accessTokenValidityInMilliseconds = 300000L;

    public void register(RegistrationRequestDto requestDto) {
        checkConfirmationPassword(requestDto.getPassword(), requestDto.getConfirmedPassword());
        checkUsername(requestDto.getUsername());
        User user = new User();
        user.setUsername(requestDto.getUsername());

        if (Objects.nonNull(requestDto.getRegionId())) {
            Region region = regionRepository.findById(requestDto.getRegionId()).orElseThrow(() ->
                    new EntityNotFoundException(Region.class, requestDto.getRegionId()));
            user.setRegion(region);
        }

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findById(userRoleId).orElseThrow(() ->
                new EntityNotFoundException(Role.class, userRoleId));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    public TokenResponseDto login(LoginRequestDto requestDto) {
        log.info("Login method called for username: " + requestDto.getUsername());

        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto();
        authenticationRequestDto.setUsername(requestDto.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequestDto,
                requestDto.getPassword());
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Login successful for username: " + requestDto.getUsername());
        return new TokenResponseDto(tokenProvider.createToken(authentication));
    }

    public String refresh(HttpServletRequest request) {
        String jwt = tokenProvider.getJWTFromRequest(request);
        try {
            Claims claims = tokenProvider.extractAllClaims(jwt);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String username = claims.get("username").toString();
            User user = userRepository.findByUsername(username).orElseThrow(() ->
                    new EntityNotFoundException(User.class, username));
            if ((new Date().getTime() - claims.getExpiration().getTime()) < accessTokenValidityInMilliseconds * 48) {
                return tokenProvider.generateToken(user);
            } else {
                throw new TokenNotValidException("Token is invalid");
            }
        } catch (JwtException ex) {
            throw new TokenNotValidException("Invalid token");
        }
        return jwt;
    }

    private void checkConfirmationPassword(String password, String confirmedPassword) {
        if (!password.equals(confirmedPassword)) {
            throw new PasswordMismatchException("Password must be same");
        }
    }

    private void checkUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }
    }

}
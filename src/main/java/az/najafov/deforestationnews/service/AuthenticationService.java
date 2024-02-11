package az.najafov.deforestationnews.service;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.LoginRequestDto;
import az.najafov.deforestationnews.dto.RegistrationRequestDto;
import az.najafov.deforestationnews.dto.TokenResponseDto;
import az.najafov.deforestationnews.mapper.UserMapper;
import az.najafov.deforestationnews.model.User;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.AuthenticationRequestDto;
import az.najafov.deforestationnews.security.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public void register(RegistrationRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
    }

    public TokenResponseDto login(LoginRequestDto requestDto) {
        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(requestDto.getUsername());
        log.info("Trying to login with username : {} ", requestDto.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequestDto,
                requestDto.getPassword());
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Username : {} logged in successfully", requestDto.getUsername());
        return new TokenResponseDto(tokenProvider.generateToken(authentication));
    }

    public String refresh(HttpServletRequest request) {
        String oldToken = tokenProvider.extractToken(request);
        String newToken = tokenProvider.createRefreshToken(oldToken);
        return newToken;
    }

}
package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.LoginRequestDto;
import az.najafov.deforestationnews.dto.RegistrationRequestDto;
import az.najafov.deforestationnews.dto.TokenResponseDto;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.security.AuthenticationRequestDto;
import az.najafov.deforestationnews.security.provider.JwtTokenProvider;
import az.najafov.deforestationnews.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setUsername("testUser");
        requestDto.setPassword("testPassword");

        authenticationService.register(requestDto);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testLogin() {
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUsername("testUser");
        requestDto.setPassword("testPassword");

        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto(requestDto.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequestDto,
                requestDto.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("testToken");

        TokenResponseDto response = authenticationService.login(requestDto);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
    }

    @Test
    void testRefresh() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(tokenProvider.extractToken(request)).thenReturn("oldToken");
        when(tokenProvider.createRefreshToken("oldToken")).thenReturn("newToken");

        String response = authenticationService.refresh(request);

        assertNotNull(response);
        assertEquals("newToken", response);
    }

}
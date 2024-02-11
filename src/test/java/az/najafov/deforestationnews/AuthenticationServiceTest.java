package az.najafov.deforestationnews;

import az.najafov.deforestationnews.dto.LoginRequestDto;
import az.najafov.deforestationnews.dto.RegistrationRequestDto;
import az.najafov.deforestationnews.dto.TokenResponseDto;
import az.najafov.deforestationnews.model.Region;
import az.najafov.deforestationnews.repository.RegionRepository;
import az.najafov.deforestationnews.repository.RoleRepository;
import az.najafov.deforestationnews.repository.UserRepository;
import az.najafov.deforestationnews.model.Role;
import az.najafov.deforestationnews.security.provider.TokenProvider;
import az.najafov.deforestationnews.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize annotated mocks
    }

    @Test
    void testRegister() {
        // Mock data
        RegistrationRequestDto requestDto = new RegistrationRequestDto();
        requestDto.setUsername("testUser");
        requestDto.setPassword("password");
        requestDto.setConfirmedPassword("password");
        requestDto.setRegionId(1L);

        // Mock behavior
        when(regionRepository.findById(any())).thenReturn(java.util.Optional.of(new Region()));
        when(roleRepository.findById(any())).thenReturn(java.util.Optional.of(new Role()));

        // Test the method
        authenticationService.register(requestDto);

        // Verify that the save method is called
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testLogin() {
// Mock data for user registration
        RegistrationRequestDto registrationRequestDto = new RegistrationRequestDto();
        registrationRequestDto.setUsername("testUser");
        registrationRequestDto.setPassword("password");
        registrationRequestDto.setConfirmedPassword("password");

        // Mock data for login
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("testUser");
        loginRequestDto.setPassword("password");

        // Mock behavior for user registration
        when(regionRepository.findById(any())).thenReturn(java.util.Optional.of(new Region()));
        when(roleRepository.findById(any())).thenReturn(java.util.Optional.of(new Role()));

        // Register the user
        authenticationService.register(registrationRequestDto);

        // Mock behavior for login
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.createToken(authentication)).thenReturn("mockToken");

        // Test the method
        TokenResponseDto responseDto = authenticationService.login(loginRequestDto);

        // Verify that the createToken method is called
        verify(tokenProvider, times(1)).createToken(authentication);
        assertNotNull(responseDto.getToken());
    }

}
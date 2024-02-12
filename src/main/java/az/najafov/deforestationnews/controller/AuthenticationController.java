package az.najafov.deforestationnews.controller;

import az.najafov.deforestationnews.common.GenericResponse;
import az.najafov.deforestationnews.dto.LoginRequestDto;
import az.najafov.deforestationnews.dto.RegistrationRequestDto;
import az.najafov.deforestationnews.dto.TokenResponseDto;
import az.najafov.deforestationnews.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "API operations for managing authentication")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates a user and provides an access token.")
    public GenericResponse<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return GenericResponse.success(authenticationService.login(loginRequestDto), "SUCCESS");
    }

    @GetMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Refreshes the user's access token using a refresh token.")
    public GenericResponse<String> refresh(HttpServletRequest request) {
        return GenericResponse.success(authenticationService.refresh(request), "SUCCESS");
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Registers a new user with the provided details.")
    public GenericResponse<Void> register(@RequestBody @Valid RegistrationRequestDto requestDto) {
        authenticationService.register(requestDto);
        return GenericResponse.success("SUCCESS");
    }

}

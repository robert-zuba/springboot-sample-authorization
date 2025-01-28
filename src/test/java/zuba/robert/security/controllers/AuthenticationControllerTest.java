package zuba.robert.security.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import zuba.robert.security.dtos.LoginUserDto;
import zuba.robert.security.dtos.RegisterUserDto;
import zuba.robert.security.entities.User;
import zuba.robert.security.responses.LoginResponse;
import zuba.robert.security.services.AuthenticationService;
import zuba.robert.security.services.JwtService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(jwtService, authenticationService);
    }

    @Test
    public void testRegister() {
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "password", "email@example.com");
        User mockUser = new User("username", "email@example.com", "password");

        when(authenticationService.signup(registerUserDto)).thenReturn(mockUser);

        // Call the method
        ResponseEntity<User> response = authenticationController.register(registerUserDto);

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        System.out.println(response.getBody().toString());
        assertEquals(mockUser, response.getBody());

        // Verify interaction with the service
        verify(authenticationService, times(1)).signup(registerUserDto);
    }

    @Test
    public void testAuthenticate() {

        LoginUserDto loginUserDto = new LoginUserDto();
        User mockUser = new User("username", "email@example.com", "password");
        String mockToken = "mockJwtToken";
        long mockExpirationTime = 3600L;

        when(authenticationService.authenticate(loginUserDto)).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn(mockToken);
        when(jwtService.getExpirationTime()).thenReturn(mockExpirationTime);

        // Call the method
        ResponseEntity<LoginResponse> response = authenticationController.authenticate(loginUserDto);

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockToken, response.getBody().getToken());
        assertEquals(mockExpirationTime, response.getBody().getExpiresIn());

        // Verify interaction with the services
        verify(authenticationService, times(1)).authenticate(loginUserDto);
        verify(jwtService, times(1)).generateToken(mockUser);
        verify(jwtService, times(1)).getExpirationTime();
    }
}

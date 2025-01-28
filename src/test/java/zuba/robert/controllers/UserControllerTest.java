package zuba.robert.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import zuba.robert.security.entities.User;
import zuba.robert.security.services.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    public void testAuthenticatedUser() {
        // Mock the authenticated user
        User mockUser = new User();
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Call the method
        ResponseEntity<User> response = userController.authenticatedUser();

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void testAllUsers() {
        // Mock the user list
        User user1 = new User();
        User user2 = new User();
        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userService.allUsers()).thenReturn(mockUsers);

        // Call the method
        ResponseEntity<List<User>> response = userController.allUsers();

        // Verify
        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockUsers, response.getBody());

        // Verify interaction with the service
        verify(userService, times(1)).allUsers();
    }

    // Clean up security context after tests
    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}

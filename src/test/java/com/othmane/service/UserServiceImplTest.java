package com.othmane.service;

import com.othmane.config.JwtProvider;
import com.othmane.model.User;
import com.othmane.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("othmane@example.com");
        user.setFullName("Othmane");
        user.setPassword("secret");
        user.setProjectSize(2);
    }

    @Test
    void testFindUserByEmail_Success() throws Exception {
        when(userRepository.findByEmail("othmane@example.com")).thenReturn(user);

        User result = userService.findUserByEmail("othmane@example.com");

        assertNotNull(result);
        assertEquals("Othmane", result.getFullName());
        verify(userRepository, times(1)).findByEmail("othmane@example.com");
    }

    @Test
    void testFindUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () ->
                userService.findUserByEmail("notfound@example.com")
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void testFindUserById_Success() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () ->
                userService.findUserById(2L)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateUserProjectSize() {
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUserProjectSize(user, 3);

        assertEquals(5, result.getProjectSize());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserProfileByJwt() throws Exception {
        String jwt = "mocked-jwt-token";
        String email = "othmane@example.com";

        mockStatic(JwtProvider.class);
        when(JwtProvider.getEmailFromToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findUserProfileByJwt(jwt);

        assertNotNull(result);
        assertEquals("othmane@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }
}

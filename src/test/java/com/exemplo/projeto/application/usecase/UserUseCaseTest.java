package com.exemplo.projeto.application.usecase;

import com.exemplo.projeto.domain.entity.User;
import com.exemplo.projeto.domain.exception.UserNotFoundException;
import com.exemplo.projeto.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userUseCase.createUser("John Doe", "john@example.com");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userUseCase.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
    }

    @Test
    void findUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userUseCase.findUserById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void findAllUsers_ShouldReturnAllUsers() {
        User anotherUser = User.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane@example.com")
                .build();
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, anotherUser));

        List<User> result = userUseCase.findAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        User updatedUser = User.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userUseCase.updateUser(1L, "John Updated", "john.updated@example.com");

        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> 
                userUseCase.updateUser(1L, "John Updated", "john.updated@example.com"));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userUseCase.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userUseCase.deleteUser(1L));
    }
}

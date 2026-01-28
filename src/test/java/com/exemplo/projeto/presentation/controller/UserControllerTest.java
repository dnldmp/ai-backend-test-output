package com.exemplo.projeto.presentation.controller;

import com.exemplo.projeto.application.usecase.UserUseCase;
import com.exemplo.projeto.domain.entity.User;
import com.exemplo.projeto.domain.exception.UserNotFoundException;
import com.exemplo.projeto.presentation.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserUseCase userUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userUseCase.createUser(any(), any())).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userUseCase.findUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(userUseCase.findUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        User anotherUser = User.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane@example.com")
                .build();
        when(userUseCase.findAllUsers()).thenReturn(Arrays.asList(testUser, anotherUser));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = User.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .build();
        when(userUseCase.updateUser(anyLong(), any(), any())).thenReturn(updatedUser);

        UserRequest updateRequest = UserRequest.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .build();

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(userUseCase).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(userUseCase.updateUser(anyLong(), any(), any()))
                .thenThrow(new UserNotFoundException(999L));

        UserRequest updateRequest = UserRequest.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .build();

        mockMvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        doThrow(new UserNotFoundException(999L)).when(userUseCase).deleteUser(999L);

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        UserRequest invalidRequest = UserRequest.builder()
                .name("John Doe")
                .email("invalid-email")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        UserRequest invalidRequest = UserRequest.builder()
                .name("")
                .email("john@example.com")
                .build();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}

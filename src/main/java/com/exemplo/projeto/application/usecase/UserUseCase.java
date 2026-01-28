package com.exemplo.projeto.application.usecase;

import com.exemplo.projeto.domain.entity.User;
import com.exemplo.projeto.domain.exception.UserNotFoundException;
import com.exemplo.projeto.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUseCase {

    private final UserRepository userRepository;

    public User createUser(String name, String email) {
        User user = User.builder()
                .name(name)
                .email(email)
                .build();
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}

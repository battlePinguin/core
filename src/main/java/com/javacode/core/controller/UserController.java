package com.javacode.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.javacode.core.entity.User;
import com.javacode.core.exception.UserNotFoundException;
import com.javacode.core.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Получение списка всех пользователей (без деталей заказов)
    @GetMapping
    @JsonView(User.Views.UserSummary.class)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Получение информации о конкретном пользователе (включая детали заказов)
    @GetMapping("/{id}")
    @JsonView(User.Views.UserDetails.class)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Validated @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

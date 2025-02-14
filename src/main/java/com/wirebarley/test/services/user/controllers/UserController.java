package com.wirebarley.test.services.user.controllers;

import com.wirebarley.test.services.user.services.UserService;
import com.wirebarley.test.services.user.models.dtos.LoginUserRequest;
import com.wirebarley.test.services.user.models.dtos.RegisterUserRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1/users")
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody @NonNull final RegisterUserRequest request) {
        final var user = userService.createUser(request.name(), request.email(), request.password());
        return ResponseEntity.ok(user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody @NonNull final LoginUserRequest request) {
        final var user = userService.login(request.email(), request.password());
        return ResponseEntity.ok(user.getId());
    }

    // TODO: Use JWT and get user id from it rather than from path.
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull final Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    // TODO: Use JWT and get user id from it rather than from path.
    @PostMapping("/{userId}/logout")
    public ResponseEntity<Long> logout(@PathVariable @NonNull final Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok().build();
    }
}

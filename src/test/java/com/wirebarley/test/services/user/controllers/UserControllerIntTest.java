package com.wirebarley.test.services.user.controllers;

import com.wirebarley.test.configuration.BaseIntTest;
import com.wirebarley.test.services.balance.repositories.BalanceRepository;
import com.wirebarley.test.services.user.models.dtos.LoginUserRequest;
import com.wirebarley.test.services.user.models.dtos.RegisterUserRequest;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.wirebarley.test.services.user.models.Status.ACTIVE;
import static com.wirebarley.test.services.user.models.Status.INACTIVE;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerIntTest extends BaseIntTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    @AfterEach
    void tearDown() {
        balanceRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUser() {
        final var name = "John Doe";
        final var email = "john.doe@email.com";
        final var password = "password";
        final var request = new RegisterUserRequest(name, email, password);

        webTestClient.post()
                .uri("/public/v1/users")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .value(userId -> {
                    assertNotNull(userId);
                    assertTrue(userId > 0);
                });
    }

    @Test
    void shouldLoginUser() {
        final var name = "John Doe";
        final var email = "john.doe@email.com";
        final var password = "password";
        final var user = userRepository.save(new User(name, email, password));
        final var request = new LoginUserRequest(email, password);

        webTestClient.post()
                .uri("/public/v1/users/login")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .value(userId -> {
                    assertNotNull(userId);
                    assertEquals(user.getId(), userId);
                });
    }

    @Test
    void shouldDeleteUser() {
        final var name = "John Doe";
        final var email = "john.doe@email.com";
        final var password = "password";
        final var user = userRepository.save(new User(name, email, password));

        assertEquals(ACTIVE, user.getStatus());

        webTestClient.delete()
                .uri("/public/v1/users/{userId}", user.getId())
                .exchange()
                .expectStatus().isOk();

        final var updatedUser = userRepository.findByEmailAndPassword(email, password).orElseThrow();
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(INACTIVE, updatedUser.getStatus());
    }
}
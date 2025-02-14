package com.wirebarley.test.services.user.services;

import com.wirebarley.test.services.balance.services.BalanceService;
import com.wirebarley.test.services.user.exceptions.InvalidUserCredentialsException;
import com.wirebarley.test.services.user.models.Status;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private BalanceService balanceService;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateNewUser() {
        final var name = "John Doe";
        final var email = "john.doe@email.com";
        final var password = "password";
        final var user = new User(1, name, email, password, Status.ACTIVE);

        doReturn(user).when(repository).save(any());
        doNothing().when(balanceService).createBalance(user.getId());

        final var result = userService.createUser(name, email, password);

        assertEquals(user, result);

        verify(repository, times(1)).save(any());
        verify(balanceService, times(1)).createBalance(user.getId());
        verifyNoMoreInteractions(repository, balanceService);
    }

    @Test
    void shouldDeleteUser() {
        final var userId = 1L;
        doNothing().when(repository).updateUserStatus(userId, Status.INACTIVE);

        userService.deleteUser(userId);

        verify(repository, times(1)).updateUserStatus(userId, Status.INACTIVE);
        verifyNoMoreInteractions(repository, balanceService);
    }

    @Test
    void shouldReturnUserOnCorrectLogin() {
        final var email = "john.doe@email.com";
        final var password = "password";
        final var user = new User(1, "John Doe", email, password, Status.ACTIVE);

        doReturn(Optional.of(user)).when(repository).findByEmailAndPassword(email, password);

        final var result = userService.login(email, password);

        assertEquals(user, result);
        verify(repository, times(1)).findByEmailAndPassword(email, password);
        verifyNoMoreInteractions(repository, balanceService);
    }

    private static Stream<Arguments> invalidUserCredentialsScenarios() {
        return Stream.of(
                Arguments.of(Optional.empty()),
                Arguments.of(Optional.of(new User(1, "John Doe", "john.doe@email.com", "password", Status.INACTIVE)))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUserCredentialsScenarios")
    void shouldThrowInvalidUserCredentialsExceptionOnIncorrectLogin(final Optional<User> user) {
        final var email = "john.doe@email.com";
        final var password = "password";
        doReturn(user).when(repository).findByEmailAndPassword(email, password);

        assertThrows(InvalidUserCredentialsException.class, () -> userService.login(email, password));

        verify(repository, times(1)).findByEmailAndPassword(email, password);
        verifyNoMoreInteractions(repository, balanceService);
    }
}
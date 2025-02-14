package com.wirebarley.test.services.user.services;

import com.wirebarley.test.services.balance.services.BalanceService;
import com.wirebarley.test.services.user.exceptions.InvalidUserCredentialsException;
import com.wirebarley.test.services.user.models.Status;
import com.wirebarley.test.services.user.models.entities.User;
import com.wirebarley.test.services.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final BalanceService balanceService;

    // TODO: Password should be encrypted. Use OAuth with JWT for authentication.
    public User createUser(@NonNull final String name, @NonNull final String email, @NonNull final String password) {
        final var user = new User(name, email, password);
        log.info("Creating user with name email {}", email);
        final var newUser = userRepository.save(user);

        log.info("Creating balance for new user created with id {}", user.getId());
        balanceService.createBalance(newUser.getId());

        return newUser;
    }

    @Transactional
    public void deleteUser(final long userId) {
        log.info("Deleting user with id {}", userId);
        userRepository.updateUserStatus(userId, Status.INACTIVE);
        logout(userId);
    }

    // TODO: Password should be encrypted. Use OAuth with JWT for authentication.
    public User login(@NonNull final String email, @NonNull final String password) throws InvalidUserCredentialsException {
        // Naive login implementation to find user by email and password
        final var user = userRepository.findByEmailAndPassword(email, password);

        if (user.isEmpty() || user.get().getStatus() == Status.INACTIVE) {
            throw new InvalidUserCredentialsException();
        }
        return user.get();
    }

    // TODO: Implement logic of invalidating JWT when logging out.
    public boolean logout(final long userId) {
        // Logout the user
        return true;
    }

//    public boolean isUserActive(final long userId) {
//        // Check if the user is active
//        return userRepository.findById(userId)
//                .map(User::getStatus)
//                .orElse(Status.INACTIVE)
//                .equals(Status.ACTIVE);
//    }
}

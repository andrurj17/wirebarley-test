package com.wirebarley.test.services.user.repositories;

import com.wirebarley.test.services.user.models.Status;
import com.wirebarley.test.services.user.models.entities.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(@NonNull final String email, @NonNull final String password);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
    void updateUserStatus(@NonNull final Long userId, @NonNull final Status status);
}

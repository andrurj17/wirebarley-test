package com.wirebarley.test.services.user.models.entities;

import com.wirebarley.test.services.user.models.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public User(@NonNull final String name, @NonNull final String email, @NonNull final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

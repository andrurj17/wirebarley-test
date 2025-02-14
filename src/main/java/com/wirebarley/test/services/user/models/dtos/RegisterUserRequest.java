package com.wirebarley.test.services.user.models.dtos;

import lombok.NonNull;

public record RegisterUserRequest(@NonNull String name, @NonNull String email, @NonNull String password) {
}

package com.wirebarley.test.services.user.models.dtos;

import lombok.NonNull;

public record LoginUserRequest(@NonNull String email, @NonNull String password) {
}

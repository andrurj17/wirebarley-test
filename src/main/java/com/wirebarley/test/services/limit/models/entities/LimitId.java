package com.wirebarley.test.services.limit.models.entities;

import com.wirebarley.test.services.limit.models.LimitType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
public class LimitId {

    private long userId;

    @NonNull
    private LimitType limitType;
}

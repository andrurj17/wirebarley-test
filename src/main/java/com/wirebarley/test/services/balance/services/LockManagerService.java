package com.wirebarley.test.services.balance.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockManagerService {
    private final Map<Long, Lock> locks = new HashMap<>();

    public Lock getLockForUser(final long userId) {
        return locks.computeIfAbsent(userId, k -> new ReentrantLock());
    }
}

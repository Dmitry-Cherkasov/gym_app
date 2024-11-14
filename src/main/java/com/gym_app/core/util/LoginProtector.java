package com.gym_app.core.util;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class LoginProtector {

    private int lockoutTime = 30000;
    private int MAX_FAILS_COUNT = 3;
    private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lockoutCache = new ConcurrentHashMap<>();



    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0) + 1;
        attemptsCache.put(username, attempts);
        if (attempts >= MAX_FAILS_COUNT) {
            lockoutCache.put(username, System.currentTimeMillis() + lockoutTime);
        }
    }

    public boolean isBlocked(String username) {
        Long unlockTime = lockoutCache.get(username);
        if (unlockTime == null) return false;
        if (System.currentTimeMillis() > unlockTime) {
            lockoutCache.remove(username);
            attemptsCache.remove(username);
            return false;
        }
        return true;
    }

    public int getLockoutTime() {
        return lockoutTime;
    }

    public void setLockoutTime(int lockoutTime) {
        this.lockoutTime = lockoutTime;
    }
}

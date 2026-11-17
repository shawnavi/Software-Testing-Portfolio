package com.team.expensetracker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Login state machine.
 *  LOGGED_OUT --correct password--> LOGGED_IN
 *  LOGGED_OUT --wrong password (3rd in a row)--> LOCKED
 *  LOGGED_IN  --logout--> LOGGED_OUT
 *  LOCKED     --admin unlock--> LOGGED_OUT
 */
public class UserAccount {
    public static final int MAX_FAILED_ATTEMPTS = 3;

    private final String username;
    private final String passwordHash;
    private AccountState state = AccountState.LOGGED_OUT;
    private int failedAttempts = 0;

    public UserAccount(String username, String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
        this.username = username;
        this.passwordHash = hash(password);
    }

    public boolean login(String password) {
        if (state == AccountState.LOCKED) {
            throw new IllegalStateException("Account is locked");
        }
        if (state == AccountState.LOGGED_IN) {
            throw new IllegalStateException("Already logged in");
        }
        if (password != null && hash(password).equals(passwordHash)) {
            state = AccountState.LOGGED_IN;
            failedAttempts = 0;
            return true;
        }
        failedAttempts++;
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            state = AccountState.LOCKED;
        }
        return false;
    }

    public void logout() {
        if (state != AccountState.LOGGED_IN) {
            throw new IllegalStateException("Not logged in");
        }
        state = AccountState.LOGGED_OUT;
    }

    public void unlock() {
        if (state != AccountState.LOCKED) {
            throw new IllegalStateException("Account is not locked");
        }
        state = AccountState.LOGGED_OUT;
        failedAttempts = 0;
    }

    public AccountState getState() {
        return state;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public String getUsername() {
        return username;
    }

    private static String hash(String text) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

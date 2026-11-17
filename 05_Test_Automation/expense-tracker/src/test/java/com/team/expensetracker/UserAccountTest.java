package com.team.expensetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Module M5 - Login and account lock (State Transition Testing). */
@DisplayName("M5 Login state transitions")
class UserAccountTest {
    private static final String PASSWORD = "Budget@2026";
    private UserAccount account;

    @BeforeEach
    void setUp() {
        account = new UserAccount("student01", PASSWORD);
    }

    @Test @DisplayName("TC-ST-01 LOGGED_OUT + correct password -> LOGGED_IN")
    void loginSuccess() {
        assertTrue(account.login(PASSWORD));
        assertEquals(AccountState.LOGGED_IN, account.getState());
    }

    @Test @DisplayName("TC-ST-02 LOGGED_OUT + 1 wrong password -> stays LOGGED_OUT")
    void oneWrong() {
        assertFalse(account.login("wrong-pass"));
        assertEquals(AccountState.LOGGED_OUT, account.getState());
        assertEquals(1, account.getFailedAttempts());
    }

    @Test @DisplayName("TC-ST-03 LOGGED_OUT + 3 wrong passwords in a row -> LOCKED")
    void lockAfterThree() {
        account.login("wrong-1");
        account.login("wrong-2");
        account.login("wrong-3");
        assertEquals(AccountState.LOCKED, account.getState());
    }

    @Test @DisplayName("TC-ST-04 2 wrong then correct -> LOGGED_IN and counter reset")
    void resetCounter() {
        account.login("wrong-1");
        account.login("wrong-2");
        assertTrue(account.login(PASSWORD));
        assertEquals(0, account.getFailedAttempts());
    }

    @Test @DisplayName("TC-ST-05 LOCKED + correct password -> login refused")
    void lockedRefusesLogin() {
        for (int i = 0; i < UserAccount.MAX_FAILED_ATTEMPTS; i++) {
            account.login("wrong");
        }
        assertThrows(IllegalStateException.class, () -> account.login(PASSWORD));
    }

    @Test @DisplayName("TC-ST-06 LOGGED_IN + logout -> LOGGED_OUT")
    void logout() {
        account.login(PASSWORD);
        account.logout();
        assertEquals(AccountState.LOGGED_OUT, account.getState());
    }

    @Test @DisplayName("TC-ST-07 LOCKED + admin unlock -> LOGGED_OUT, then login works")
    void unlock() {
        for (int i = 0; i < UserAccount.MAX_FAILED_ATTEMPTS; i++) {
            account.login("wrong");
        }
        account.unlock();
        assertEquals(AccountState.LOGGED_OUT, account.getState());
        assertTrue(account.login(PASSWORD));
    }

    @Test @DisplayName("TC-ST-08 invalid transition: logout while LOGGED_OUT is refused")
    void logoutWhenLoggedOut() {
        assertThrows(IllegalStateException.class, () -> account.logout());
    }

    @Test @DisplayName("TC-ST-09 invalid transition: unlock an account that is not locked is refused")
    void unlockWhenNotLocked() {
        assertThrows(IllegalStateException.class, () -> account.unlock());
    }

    @Test @DisplayName("TC-ST-10 invalid transition: login while already LOGGED_IN is refused")
    void loginTwice() {
        account.login(PASSWORD);
        assertThrows(IllegalStateException.class, () -> account.login(PASSWORD));
    }

    @Test @DisplayName("TC-ST-11 password shorter than 8 characters is rejected at sign-up")
    void shortPassword() {
        assertThrows(ValidationException.class, () -> new UserAccount("student02", "short"));
    }
}

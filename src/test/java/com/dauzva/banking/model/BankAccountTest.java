package com.dauzva.banking.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.dauzva.banking.exception.InsufficientFundsException;

class BankAccountTest {

    @Test
    @DisplayName("Should create a new bank account with zero balance")
    void shouldCreateAccountWithZeroBalance() {
        User user = new User();
        BankAccount account = new BankAccount("ACC123", user);
        assertEquals(0.0, account.getBalance(), "New account balance should be 0.0");
        assertEquals("ACC123", account.getAccountNumber(), "Account number should match constructor");
        assertNotNull(account.getUser(), "User should be set");
    }

    @Test
    @DisplayName("Should successfully deposit positive amount")
    void shouldDepositPositiveAmount() {
        User user = new User();
        BankAccount account = new BankAccount("ACC123", user);
        account.deposit(100.50);
        assertEquals(100.50, account.getBalance(), 0.001, "Balance should increase by deposited amount");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for negative deposit amount")
    void shouldThrowExceptionForNegativeDeposit() {
        User user = new User();
        BankAccount account = new BankAccount("ACC123", user);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-50.0),
                "Should throw IllegalArgumentException for negative deposit");
    }

    @Test
    @DisplayName("Should successfully withdraw valid amount")
    void shouldWithdrawValidAmount() {
        User user = new User();
        BankAccount account = new BankAccount("ACC123", user);
        account.deposit(200.0);
        account.withdraw(50.0);
        assertEquals(150.0, account.getBalance(), 0.001, "Balance should decrease by withdrawn amount");
    }

    @Test
    @DisplayName("Should not withdraw if insufficient balance")
    void shouldNotWithdrawIfInsufficientBalance() {
        User user = new User();
        BankAccount account = new BankAccount("ACC123", user);
        account.deposit(100.0);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(150.0),
                "Should throw InsufficientFundsException for withdrawal exceeding balance");
        assertEquals(100.0, account.getBalance(), 0.001, "Balance should remain unchanged after failed withdrawal");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException for negative withdrawal amount")
    void shouldThrowExceptionForNegativeWithdrawal() {
        User user = new User();
        BankAccount account = new BankAccount("ACC123", user);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-20.0),
                "Should throw IllegalArgumentException for negative withdrawal");
    }
}
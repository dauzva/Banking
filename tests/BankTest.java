import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @Test
    void getBankAccounts() {
        Bank bank = new Bank();
        assertNotNull(bank.getBankAccounts(), "Bank accounts map should not be null");
        assertTrue(bank.getBankAccounts().isEmpty(), "Bank accounts map should be empty initially");
    }

    @Test
    void addBankAccount() {
        Bank bank = new Bank();
        BankAccount account = new BankAccount(100.0);
        bank.addBankAccount("123", account);

        assertNotNull(bank.getBankAccounts().get("123"), "Bank account should be added to the bank");
        assertEquals(100.0, bank.getBankAccounts().get("123").getBalance(), "Balance should match the initial value");
    }

    @Test
    void getAccount() {
        Bank bank = new Bank();
        BankAccount account = new BankAccount(200.0);
        bank.addBankAccount("456", account);

        BankAccount retrievedAccount = bank.getAccount("456");
        assertNotNull(retrievedAccount, "Retrieved account should not be null");
        assertEquals(200.0, retrievedAccount.getBalance(), "Retrieved balance should match the initial value");

        BankAccount nonExistentAccount = bank.getAccount("999");
        assertNull(nonExistentAccount, "Retrieving a non-existent account should return null");
    }
}
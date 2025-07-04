import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, BankAccount> bankAccounts;

    public Bank() {
        bankAccounts = new HashMap<>();
    }

    public Map<String, BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void addBankAccount(String id, BankAccount bankAccount) {
        bankAccounts.put(id, bankAccount);
    }

    public BankAccount getAccount(String id) {
        return bankAccounts.get(id);
    }

}

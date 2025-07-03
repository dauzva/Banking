public class BankAccount {
    private double balance;

    public double getBalance() {
        return balance;
    }

    public BankAccount() {
        this.balance = 0;
    }

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance+=amount;
    }

    public void withdraw(double amount) {
        if (balance - amount >= 0) {
            balance -= amount;
        }
        else {
            System.out.println("Insufficient balance.");
        }
    }

    public void printBalance() {
        System.out.printf("Current balance: %.2f €", balance);
    }

    public void transferTo(BankAccount destination, double amount) {
        // TODO: make sure that the transfer is transactional, meaning both operations must succeed
        this.withdraw(amount);
        destination.deposit(amount);
    }
}
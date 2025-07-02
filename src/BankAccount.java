public class BankAccount {
    private double balance;

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
        balance-=amount;
    }

    public void printBalance() {
        System.out.printf("Current balance: %.2f €", balance);
    }

    public void transferTo(BankAccount destination, double amount) {
        this.withdraw(amount);
        destination.deposit(amount);
    }
}
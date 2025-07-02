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
        System.out.println("Current balance: "+balance);
    }

    public void transferTo(BankAccount destination, double amount) {
        this.withdraw(amount);
        destination.deposit(amount);
    }
}
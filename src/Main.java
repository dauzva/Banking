import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        bank.addBankAccount("111", new BankAccount());
        bank.addBankAccount("222", new BankAccount(200));
        bank.addBankAccount("333", new BankAccount(300));

        BankAccount account = bank.getAccount("111");

        boolean running = true;

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1 - Deposit");
            System.out.println("2 - Withdraw");
            System.out.println("3 - Check Balance");
            System.out.println("4 - Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;
                case 3:
                    account.printBalance();
                    break;
                case 4:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();

        File dataFile = new File("data/accounts.csv");

        try {
            File parentDir = dataFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileWriter writer = new FileWriter(dataFile);

            writer.write("AccountIndex,Balance\n");
            bank.getBankAccounts().forEach((id, bankAccount) -> {
                try {
                    writer.write( id+ "," + String.format("%.2f", bankAccount.getBalance()) + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.close();
            System.out.println("Account data saved to " + dataFile.getPath());
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
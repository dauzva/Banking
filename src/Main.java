import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount());
        accounts.add(new BankAccount(200));
        accounts.add(new BankAccount(300));
        accounts.add(new BankAccount(100));

        BankAccount account = accounts.get(0); // starts with 0 balance

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
            for (int i = 0; i < accounts.size(); i++) {
                writer.write(i + "," + String.format("%.2f", accounts.get(i).getBalance()) + "\n");
            }

            writer.close();
            System.out.println("Account data saved to " + dataFile.getPath());
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
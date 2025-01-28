package edu.kit.kastel.debugging.banking;

public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public void deposit(double amount) {
        balance = amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
        if (balance < 0) {
            System.out.println("Insufficient funds");
        }
    }

    public double getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000.0);
        System.out.println("Initial balance: " + account.getBalance());

        account.deposit(500.0);
        System.out.println("Balance after deposit: " + account.getBalance());

        account.withdraw(2000.0);
        System.out.println("Balance after withdrawal: " + account.getBalance());
    }
}
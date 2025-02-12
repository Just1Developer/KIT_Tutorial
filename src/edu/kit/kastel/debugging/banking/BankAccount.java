package edu.kit.kastel.debugging.banking;

import java.util.Objects;

public class BankAccount {
    private double balance;
    int i;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
        i = 5;
    }

    public void deposit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        balance += amount;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Double.compare(balance, that.balance) == 0 && i == that.i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, i);
    }
}
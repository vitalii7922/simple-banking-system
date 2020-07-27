package banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static final Map<Long, Account> accounts = new HashMap<>();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final Random random = new Random();
    private static boolean exit = false;

    public static void main(String[] args) throws IOException {

        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            String command = reader.readLine();
            switch (command) {
                case "1":
                    createAccount();
                    System.out.println();
                    break;
                case "2":
                    System.out.println("\nEnter your card number:");
                    long cardNumber = Long.parseLong(reader.readLine());
                    System.out.println("Enter your PIN:");
                    String numberPIN = reader.readLine();
                    Account account = logIn(cardNumber, numberPIN);
                    if (account != null) {
                        System.out.println("\nYou have successfully logged in!\n");
                        inAccount(account);
                        if (exit) {
                            System.out.println("\nBye!");
                            return;
                        }
                    } else {
                        System.out.println("\nWrong card number or PIN!");
                    }
                    break;
                case "0":
                    System.out.println("\nBye!");
                    return;
                default:
                    break;
            }
        }
    }

    private static void inAccount(Account account) throws IOException {
        while (true) {
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
            String command = reader.readLine();
            switch (command) {
                case "1":
                    System.out.printf("%nBalance: %d%n%n", account.getBalance());
                    break;
                case "2":
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                case "0":
                    exit = true;
                    return;
                default:
                    break;
            }
        }
    }

    private static void createAccount() {
        
        String cardNumber = "400000" + generateRandomNumber(10);
        Account account = new Account(cardNumber, generateRandomNumber(4));
        accounts.put(Long.valueOf(cardNumber), account);
        System.out.println("\nYour card has been created");
        System.out.printf("Your card number: %n%s%n", account.getCardNumber());
        System.out.printf("Your card PIN: %n%s%n", account.getCardPIN());
    }

    private static String generateRandomNumber(int length) {
        StringBuilder randomNumber = new StringBuilder();
        while (true) {
            for (int i = 0; i < length; i++) {
                randomNumber.append(random.nextInt(10));
            }
            if (accounts.get(Long.parseLong(randomNumber.toString())) == null) {
                return randomNumber.toString();
            }
        }
    }

    private static Account logIn(long cardNumber, String cardPIN) {
        Account account = accounts.get(cardNumber);
        if (account != null && account.getCardPIN().equals(cardPIN)) {
            return account;
        }
        return null;
    }
}

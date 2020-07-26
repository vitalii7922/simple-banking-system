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
        label:
        do {
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
                    int numberPIN = Integer.parseInt(reader.readLine());
                    Account account = logIn(cardNumber, numberPIN);
                    if (account != null) {
                        System.out.println("\nYou have successfully logged in!\n");
                        inAccount(account);
                        if (exit) {
                            break label;
                        }
                    } else {
                        System.out.println("\nWrong card number or PIN!");
                    }
                    break;
                case "0":
                    break label;
                default:
                    break;
            }
        } while (true);
        System.out.println("\nBye!");
    }

    private static void inAccount(Account account) throws IOException {
        label:
        while (true) {
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
            String command = reader.readLine();
            switch (command) {
                case "1":
                    System.out.println();
                    System.out.printf("Balance: %d%n", account.getBalance());
                    break;
                case "2":
                    System.out.println("You have successfully logged out!");
                    break label;
                case "0":
                    exit = true;
                    break label;
                default:
                    break;
            }
        }
    }

    private static void createAccount() {
        String cardNumber = "400000" + generateRandomNumber(10);
        Account account = new Account(cardNumber, Integer.parseInt(generateRandomNumber(4)));
        accounts.put(Long.valueOf(cardNumber), account);
        System.out.println("\nYour card has been created");
        System.out.printf("Your card number: %n%s%n", account.getCardNumber());
        System.out.printf("Your card PIN: %n%d%n", account.getCardPIN());
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

/*    private static int generateRandomPIN() {
        return (int) (Math.random() * 9999);
    }*/

    private static Account logIn(long cardNumber, int cardPIN) {
        Account account = accounts.get(cardNumber);
        if (account != null && account.getCardPIN() == cardPIN) {
            return account;
        }
        return null;
    }
}

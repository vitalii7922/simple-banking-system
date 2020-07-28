package banking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final Random random = new Random();
    private static boolean exit = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        DBOperations.setUrl(args[1]);
        DBOperations.createNewTable();
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
                    String cardNumber = reader.readLine();
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
                        System.out.println("\nWrong card number or PIN!\n");
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
        String identificationNumber = generateRandomNumber(9);
        String cardNumber = applyLuhnAlgorithm(identificationNumber);
        Account account = new Account(cardNumber, generateRandomNumber(4));
        DBOperations.insert(account);
        System.out.println("\nYour card has been created");
        System.out.printf("Your card number: %n%s%n", account.getCardNumber());
        System.out.printf("Your card PIN: %n%s%n", account.getCardPIN());
//        DBOperations.selectAll();
    }

    private static String generateRandomNumber(int length) {
        StringBuilder randomNumber = new StringBuilder();
        while (true) {
            for (int i = 0; i < length; i++) {
                randomNumber.append(random.nextInt(10));
            }
            if (!DBOperations.selectCardByNumber(randomNumber.toString())) {
                return randomNumber.toString();
            }
        }
    }

    private static Account logIn(String cardNumber, String cardPIN) {
        Account account = DBOperations.selectCard(cardNumber, cardPIN);
        if (account != null && account.getCardNumber() != null) {
            return account;
        }
        return null;
    }

    private static String applyLuhnAlgorithm(String accountIdentifier) {
        String cardNumber = "400000" + accountIdentifier;
        char[] numbers = cardNumber.toCharArray();
        List<Integer> algorithmResult = new ArrayList<>();
        for (int i = 0; i < cardNumber.length(); i++) {
            int number = Character.getNumericValue(numbers[i]);
            if ((i + 1) % 2 != 0) {
                number *= 2;
            }
            algorithmResult.add(number);
        }
        algorithmResult = algorithmResult
                .stream()
                .map(x -> x > 9 ? x - 9 : x)
                .collect(Collectors.toList());
        return addCheckSumNumber(algorithmResult
                .stream()
                .mapToInt(Integer::intValue)
                .sum(), cardNumber);
    }

    private static String addCheckSumNumber(int cardNumberSum, String cardNumber) {
        for (int i = 0; i < 10; i++) {
            if ((cardNumberSum + i) % 10 == 0) {
                return cardNumber + i;
            }
        }
        return cardNumber;
    }
}

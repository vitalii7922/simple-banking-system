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
//        DBOperations.dropTable();
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
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            String command = reader.readLine();
            switch (command) {
                case "1":
                    account = DBOperations.selectCardById(account.getId());
                    assert account != null;
                    System.out.printf("%nBalance: %d%n%n", account.getBalance());
                    break;
                case "2":
                    System.out.println("\nEnter income:");
                    addIncome(reader.readLine(), DBOperations.selectCardById(account.getId()));
                    System.out.println("Income was added\n");
                    break;
                case "3":
                    System.out.println("\nTransfer");
                    System.out.println("Enter card number:");
                    String toAccount = reader.readLine();
                    doTransfer(toAccount, DBOperations.selectCardById(account.getId()));
                    break;
                case "0":
                    exit = true;
                    return;
                default:
                    break;
            }
        }
    }

    private static void doTransfer(String toAccountNumber, Account fromAccount) throws IOException {
        if (!toAccountNumber.equals(applyLuhnAlgorithm(toAccountNumber.substring(0, 15)))) {
            System.out.println(toAccountNumber);
            System.out.println(toAccountNumber.substring(0, 16));
            System.out.println("Probably you made mistake in the card number. Please try again!\n");
            return;
        }
        Account toAccount = DBOperations.selectCardByNumber(toAccountNumber);
        if (toAccount == null) {
            System.out.println("Such a card doesn't exist\n");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            int money = Integer.parseInt(reader.readLine());
            if (money > fromAccount.getBalance()) {
                System.out.println("Not enough money\n");
            } else {
                DBOperations.decBalance(money, fromAccount);
                DBOperations.incBalance(money, toAccount);
                System.out.println("Success\n");
            }
        }
    }

    private static synchronized void createAccount() {
        String identificationNumber = generateRandomNumber(9);
        String cardNumber = applyLuhnAlgorithm(identificationNumber);
//        System.out.println(DBOperations.getCardsAmount());
        Account account = new Account(DBOperations.getCardsAmount() + 1, cardNumber, generateRandomNumber(4));
        DBOperations.insert(account);
        System.out.println("\nYour card has been created");
        System.out.printf("Your card number: %n%s%n", account.getCardNumber());
        System.out.printf("Your card PIN: %n%s%n", account.getCardPIN());
        DBOperations.selectAll();
    }

    private static void addIncome(String income, Account account) {
        DBOperations.incBalance(Integer.parseInt(income), account);
//        DBOperations.selectAll();
    }

    private static String generateRandomNumber(int length) {
        StringBuilder randomNumber = new StringBuilder();
        while (true) {
            for (int i = 0; i < length; i++) {
                randomNumber.append(random.nextInt(10));
            }
            if (length == 9 && !DBOperations.isDuplicate(randomNumber.insert(0, "400000").toString())) {
                return randomNumber.toString();
            } else if (length == 4) {
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

    private static String applyLuhnAlgorithm(String cardNumber) {
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

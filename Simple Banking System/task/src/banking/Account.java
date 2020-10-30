package banking;

/**
 * User's banking account
 */
public class Account {
    private long id;
    private String cardNumber;
    private String cardPIN;
    private int balance;

    Account() {
    }

    Account(String cardNumber, String cardPIN) {
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        balance = 0;
    }

    public Account(long id, String cardNumber, String cardPIN) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        this.balance = 0;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    String getCardNumber() {
        return cardNumber;
    }

    void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    String getCardPIN() {
        return cardPIN;
    }

    void setCardPIN(String cardPIN) {
        this.cardPIN = cardPIN;
    }

    int getBalance() {
        return balance;
    }

    void setBalance(int balance) {
        this.balance = balance;
    }
}

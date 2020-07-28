package banking;

public class Account {
    private long id;
    private String cardNumber;
    private String cardPIN;
    private int balance;

    public Account() {
    }

    public Account(String cardNumber, String cardPIN) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPIN() {
        return cardPIN;
    }

    public void setCardPIN(String cardPIN) {
        this.cardPIN = cardPIN;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

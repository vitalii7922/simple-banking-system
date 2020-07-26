package banking;

public class Account {
    String cardNumber;
    int cardPIN;
    int balance;

    public Account(String cardNumber, int cardPIN) {
        this.cardNumber = cardNumber;
        this.cardPIN = cardPIN;
        balance = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardPIN() {
        return cardPIN;
    }

    public void setCardPIN(int cardPIN) {
        this.cardPIN = cardPIN;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

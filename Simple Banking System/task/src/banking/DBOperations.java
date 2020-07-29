package banking;

import java.sql.*;

public class DBOperations {

    private static String url;
    private static final String NUMBER = "number";
    private static final String BALANCE = "balance";

    private DBOperations() {
    }

    public static void setUrl(String fileName) {
        DBOperations.url = "jdbc:sqlite:" + fileName;
    }

    public static void insert(Account account) {
        String sql = "INSERT INTO card(id, number,pin) VALUES(?, ?,?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(2, account.getCardNumber());
            pstmt.setString(3, account.getCardPIN());
            pstmt.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Account selectCard(String cardNumber, String pin) {
        String sql = "SELECT id, number, pin FROM card WHERE number = ? AND pin = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);
            Account account = new Account();
            // loop through the result set
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    account.setId(rs.getLong("id"));
                    account.setCardNumber(rs.getString(NUMBER));
                    account.setCardPIN(rs.getString("pin"));
                }
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Account selectCardByNumber(String cardNumber) {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            // loop through the result set
            Account account = null;
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    account = new Account();
                    account.setId(rs.getLong("id"));
                    account.setCardNumber(rs.getString(NUMBER));
                    account.setCardPIN(rs.getString("pin"));
                    account.setBalance(rs.getInt(BALANCE));
                }
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean isDuplicate(String cardNumber) {
        String sql = "SELECT number FROM card";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                if (rs.getString(NUMBER).substring(0, 15).equals(cardNumber)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static Account selectCardById(long id) {
        String sql = "SELECT number, pin, balance  FROM card WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            Account account = new Account();
            // loop through the result set
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    account.setId(id);
                    account.setCardNumber(rs.getString(NUMBER));
                    account.setCardPIN(rs.getString("pin"));
                    account.setBalance(rs.getInt(BALANCE));
                }
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void selectAll() {
        String sql = "SELECT id, number, pin, balance FROM card";
        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString(NUMBER) + "\t" +
                        rs.getString(BALANCE) + "\t" +
                        rs.getString("pin"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card ("
                + "	id INTEGER PRIMARY KEY,"
                + "	number TEXT,"
                + "	pin TEXT,"
                + "	balance INTEGER DEFAULT 0"
                + ");";
        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void incBalance(int income, Account account) {
        String query = "UPDATE card SET balance = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, account.getBalance() + (long) income);
            preparedStatement.setLong(2, account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void decBalance(int expenses, Account account) {
        String query = "UPDATE card SET balance = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, account.getBalance() - (long) expenses);
            preparedStatement.setLong(2, account.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAccount(long id) {
        String sql = "DELETE FROM card WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTable() {
        String sql = "DROP TABLE card";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

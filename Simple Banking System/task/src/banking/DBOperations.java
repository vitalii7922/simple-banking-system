package banking;

import java.sql.*;

public class DBOperations {

    private static String url;

    private DBOperations() {
    }

    public static void setUrl(String fileName) {
        DBOperations.url = "jdbc:sqlite:" + fileName;
    }

    private static Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * @param
     * @param
     */
    public static void insert(Account account) {
        String sql = "INSERT INTO card(id, number,pin) VALUES(?, ?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, account.getId());
            pstmt.setString(2, account.getCardNumber());
            pstmt.setString(3, account.getCardPIN());
            pstmt.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Account selectCard(String cardNumber, String pin) {
        String sql = "SELECT id, number, pin FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);
            Account account = new Account();
            // loop through the result set
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                account.setId(rs.getLong("id"));
                account.setCardNumber(rs.getString("number"));
                account.setCardPIN(rs.getString("pin"));
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static Account selectCardByNumber(String cardNumber) {
        String sql = "SELECT id, number, pin, balance FROM card WHERE number = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            // loop through the result set
            ResultSet rs = pstmt.executeQuery();
            Account account = null;
            while (rs.next()) {
                account = new Account();
                account.setId(rs.getLong("id"));
                account.setCardNumber(rs.getString("number"));
                account.setCardPIN(rs.getString("pin"));
                account.setBalance(rs.getInt("balance"));
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean isDuplicate(String cardNumber) {
        String sql = "SELECT number FROM card WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            // loop through the result set
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("number").substring(0, 15).equals(cardNumber)) {
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

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            Account account = new Account();
            // loop through the result set
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                account.setId(id);
                account.setCardNumber(rs.getString("number"));
                account.setCardPIN(rs.getString("pin"));
                account.setBalance(rs.getInt("balance"));
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void selectAll() {
        String sql = "SELECT id, number, pin, balance FROM card";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("number") + "\t" +
                        rs.getString("balance") + "\t" +
                        rs.getString("pin"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getCardsAmount() {
        String sql = "SELECT count(*) FROM card;";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static void createNewTable() {
        // SQLite connection string
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS card ("
                + "	id INTEGER,"
                + "	number TEXT,"
                + "	pin TEXT,"
                + "	balance INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void incBalance(int income, Account account) {
        String query = "UPDATE card SET balance = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // set the corresponding param
            preparedStatement.setLong(1, account.getBalance() + income);
            preparedStatement.setLong(2, account.getId());
            // update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void decBalance(int expenses, Account account) {
        String query = "UPDATE card SET balance = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // set the corresponding param
            preparedStatement.setLong(1, account.getBalance() - expenses);
            preparedStatement.setLong(2, account.getId());
            // update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAccount(long id) {
        String sql = "DELETE FROM card WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setLong(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTable() {
        String sql = "DROP TABLE card";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            // execute the delete statement
            pstmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

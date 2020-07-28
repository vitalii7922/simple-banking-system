package banking;

import java.sql.*;

public class DBOperations {

    private static String url;

    private DBOperations() {
    }

    public static void setUrl(String fileName) {
        DBOperations.url = "jdbc:sqlite:" + fileName;
    }

    /**
     * @param
     * @param
     */
    public static void insert(Account account) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getCardNumber());
            pstmt.setString(2, account.getCardPIN());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Account selectCard(String cardNumber, String pin) {
        String sql = "SELECT number, pin FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);
            Account account = new Account();
            // loop through the result set
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                account.setCardNumber(rs.getString("number"));
                account.setCardPIN(rs.getString("pin"));
            }
            return account;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static boolean selectCardByNumber(String cardNumber) {
        String sql = "SELECT number FROM card WHERE number = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            Account account = new Account();
            // loop through the result set
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    public static void selectAll() {
        String sql = "SELECT id, number, pin FROM card";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("number") + "\t" +
                        rs.getString("pin"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete(int id) {
        String sql = "DELETE FROM card WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTable() {
        String sql = "DROP TABLE card";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            // execute the delete statement
            pstmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

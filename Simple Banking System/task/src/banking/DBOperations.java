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
        String sql = "INSERT INTO card(id,number,pin) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, account.getId());
            pstmt.setString(2, account.getCardNumber());
            pstmt.setString(3, account.getCardPIN());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Account selectCard(String cardNumber, String pin) {
        String sql = "SELECT number, pin, FROM card WHERE number = ? AND pin = ?";

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
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	number text NOT NULL,\n"
                + "	pin text NOT NULL\n"
                + "	balance integer default 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

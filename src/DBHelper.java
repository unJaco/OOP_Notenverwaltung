import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

    static Connection c = null;

    static final String sqlCredentials = "CREATE TABLE IF NOT EXISTS CREDENTIALS " +
            "( EMAIL           TEXT    UNIQUE NOT NULL, " +
            " PASSWORD           TEXT    NOT NULL)";

    static final String sqlUser = "CREATE TABLE IF NOT EXISTS USER " +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NAME           TEXT    NOT NULL, " +
            " VORNAME           TEXT    NOT NULL, " +
            " EMAIL           TEXT    UNIQUE NOT NULL, " +
            " ROLE            INT     NOT NULL)";

    static void connectToDb() throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:NOTENVERWALTUNG.db");

            executeSqlStatement(sqlUser);
            executeSqlStatement(sqlCredentials);

        } catch (Exception e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            c.close();
            System.exit(0);

        }

    }

    static void executeSqlStatement(String sql) throws SQLException {
        Statement statement;
        statement = c.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

}

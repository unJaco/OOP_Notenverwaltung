package db;

import classes.User;
import classes.User;

import java.sql.*;

public class DBHelper {

    static Connection c = null;

    static final String sqlCredentials = "CREATE TABLE IF NOT EXISTS CREDENTIALS " +
            "( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "EMAIL           TEXT    UNIQUE NOT NULL, " +
            " PASSWORD           TEXT    NOT NULL);";

    static final String sqlUser = "CREATE TABLE IF NOT EXISTS USER " +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NAME           TEXT    NOT NULL, " +
            " VORNAME           TEXT    NOT NULL, " +
            " EMAIL           TEXT    UNIQUE NOT NULL, " +
            " ROLE            INT     NOT NULL);";

    static final String sqlInsertUser = "INSERT INTO CREDENTIALS(EMAIL, PASSWORD) VALUES(?,?);";

    public static void connectToDb() throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:NOTENVERWALTUNG.db");

            System.out.println("lol");


            executeSqlStatement(sqlCredentials);
            executeSqlStatement(sqlUser);

        } catch (Exception e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            c.close();
            System.exit(0);

        }

    }

    public static void executeSqlStatement(String sql) throws SQLException {
        Statement statement;
        statement = c.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }

    static ResultSet executeSqlSelectStatement(String sql) throws SQLException {

        Statement statement;
        statement = c.createStatement();
        return statement.executeQuery(sql);
    }

    public static boolean tryToLogin(String email, String password) throws SQLException {

        String sql = "SELECT * FROM CREDENTIALS WHERE (EMAIL = '" + email + "' AND PASSWORD = '" + password + "');";
        ResultSet rs = executeSqlSelectStatement(sql);
        return rs.next();
    }

    public static boolean insertUser(User user) throws SQLException {

        PreparedStatement preparedStatement;
        preparedStatement = c.prepareStatement(sqlInsertUser);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getFirstname() + "." + user.getLastname());

        return preparedStatement.executeUpdate() == 2;

    }

}

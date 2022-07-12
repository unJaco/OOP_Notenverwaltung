package src.main.java.db;


import src.main.java.classes.*;

import java.sql.*;

public class DBHelper {

    static Connection c = null;

    static final String sqlTableCredentials = "CREATE TABLE IF NOT EXISTS CREDENTIALS " +
            "( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "EMAIL           TEXT    UNIQUE NOT NULL, " +
            " PASSWORD           TEXT    NOT NULL);";

    static final String sqlTableUser = "CREATE TABLE IF NOT EXISTS USER " +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NAME           TEXT    NOT NULL, " +
            " VORNAME           TEXT    NOT NULL, " +
            " ROLE            INT     NOT NULL);";

    static final String sqlTableGrades = "CREATE TABLE IF NOT EXISTS GRADES " +
            "(GRADE_ID INTEGER PRIMARY KEY AUTOINCREMENT " +
            "USER_ID INT NOT NULL " +
            "SCHOOLCLASS TEXT NOT NULL" +
            "SUBJECT TEXT NOT NULL" +
            "GRADE_BEZ TEXT NOT NULL" +
            "GRADE_VAL INT NOT NULL);";

    //static final String sqlTableSchoolClasses = "CREATE TABLE IF NOT EXISTS SCHOOLCLASSES (BEZEICHNUNG TEXT UNIQUE NOT NULL, "

    static final String sqlInsertUserCredentials = "INSERT INTO CREDENTIALS(EMAIL, PASSWORD) VALUES(?,?);";

    static final String sqlInsertUserData = "INSERT INTO USER(NAME, VORNAME, ROLE) VALUES(?,?,?);";

    static final String sqlInsertGrade = "INSERT INTO GRADES(USER_ID, SCHOOLCLASS, SUBJECT, GRADE_BEZ, GRADE_VAL) VALUES(?,?,?,?,?);";

    public static void connectToDb() throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:NOTENVERWALTUNG.db");

            executeSqlStatement("DROP TABLE CREDENTIALS");
            executeSqlStatement(sqlTableCredentials);
            executeSqlStatement(sqlTableUser);
            executeSqlStatement(sqlTableGrades);


            insertUser(new Student(null, "Jac", "Mei", Role.STUDENT), "mail");
            insertUser(new Teacher(null, "Teacher", "Test", Role.TEACHER), "teacher");

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

    public static User tryToLogin(String email, String password) throws SQLException {

        String sqlCred = "SELECT * FROM CREDENTIALS WHERE (EMAIL = '" + email + "' AND PASSWORD = '" + password + "');";
        ResultSet rsCred = executeSqlSelectStatement(sqlCred);

        if (rsCred.next()) {
            String id = String.valueOf(rsCred.getInt("ID"));
            rsCred.close();

            String sqlUser = "SELECT * FROM USER WHERE(ID = '" + id + "');";
            ResultSet rsUser = executeSqlSelectStatement(sqlUser);
            String role = rsUser.getString("ROLE");


            switch (role) {
                case "STUDENT" -> {
                    return new Student(rsUser);
                }

                case "TEACHER" -> {
                    return new Teacher(rsUser);
                }

                case "ADMIN" -> {
                    return new Admin(rsUser);
                }

                default -> {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public static boolean insertUser(User user, String email) throws SQLException {

        PreparedStatement pSCred;
        PreparedStatement pSData;

        pSCred = c.prepareStatement(sqlInsertUserCredentials);
        pSData = c.prepareStatement(sqlInsertUserData);

        pSCred.setString(1, email);
        pSCred.setString(2, user.getFirstname() + "." + user.getLastname());

        pSData.setString(1, user.getFirstname());
        pSData.setString(2, user.getLastname());
        pSData.setString(3, user.getRole().name());

        int cred = pSCred.executeUpdate();
        pSCred.close();

        int data = pSData.executeUpdate();
        pSData.close();

        return cred == 2 && data == 3;
    }


    /*
        TODO subject and schoolclass
    */
    public static boolean insertGrade(Grade grade, Student student, Subject subject, String schoolclass) throws SQLException {

        PreparedStatement pSGrade;

        pSGrade = c.prepareStatement(sqlInsertGrade);

        pSGrade.setInt(1, student.getId());
        pSGrade.setString(2, schoolclass);
        pSGrade.setString(3, subject.name());
        pSGrade.setString(4, grade.getGrade_bez());
        pSGrade.setInt(5, grade.getGrade_val());


        int insertGrade = pSGrade.executeUpdate();

        return insertGrade == 5;
    }

}

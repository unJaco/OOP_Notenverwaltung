package src.main.java.db;


import src.main.java.classes.*;

import java.sql.*;

import javax.security.auth.Subject;

public class DBHelper {

    static Connection c = null;

    static final String sqlTableCredentials = "CREATE TABLE IF NOT EXISTS CREDENTIALS " +
            "(UID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "EMAIL           TEXT    UNIQUE NOT NULL, " +
            " PASSWORD           TEXT    NOT NULL);";

    static final String sqlTableUser = "CREATE TABLE IF NOT EXISTS USER " +
            "(UID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NAME           TEXT    NOT NULL, " +
            " VORNAME           TEXT    NOT NULL, " +
            " ROLE            INT     NOT NULL);";

    static final String sqlTableGrades = "CREATE TABLE IF NOT EXISTS GRADES " +
            "(GRADE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "UID INT NOT NULL, " +
            "SCHOOLCLASS TEXT NOT NULL," +
            "SUBJECT TEXT NOT NULL," +
            "GRADE_BEZ TEXT NOT NULL," +
            "GRADE_VAL INT NOT NULL);";

    static final String sqlTableStudents = "CREATE TABLE IF NOT EXISTS STUDENTS " +
            "(UID INT NOT NULL," +
            "CLASS_ID TEXT NOT NULL);";

    static final String sqlTableTeacher = "CREATE TABLE IF NOT EXISTS TEACHER " +
            "(UID INT NOT NULL," +
            "CLASS_ID TEXT NOT NULL," +
            "SUBJECT TEXT NOT NULL);";

    //static final String sqlTableSchoolClasses = "CREATE TABLE IF NOT EXISTS SCHOOLCLASSES (BEZEICHNUNG TEXT UNIQUE NOT NULL, "

    static final String sqlInsertUserCredentials = "INSERT INTO CREDENTIALS(EMAIL, PASSWORD) VALUES(?,?);";

    static final String sqlInsertUserData = "INSERT INTO USER(NAME, VORNAME, ROLE) VALUES(?,?,?);";

    static final String sqlInsertGrade = "INSERT INTO GRADES(UID, SCHOOLCLASS, SUBJECT, GRADE_BEZ, GRADE_VAL) VALUES(?,?,?,?,?);";

    static final String sqlAddStudentToClass = "INSERT INTO STUDENTS (UID, CLASS_ID) VALUES(?,?);";

    static final String sqlAddTeacherWithSubjectToClass = "INSERT INTO TEACHER (UID, CLASS_ID, SUBJECT) VALUES(?,?, ?);";

    static final String sqlDeleteUserCredentials = "DELETE FROM CREDENTIALS WHERE ID=() Values(?);";

    static final String sqlDeleteUserData = "DELETE FROM USER WHERE ID=() Values(?);";

    public static void connectToDb() throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:NOTENVERWALTUNG.db");


            //executeSqlStatement("DROP TABLE CREDENTIALS");
            //executeSqlStatement("DROP TABLE USER");


            executeSqlStatement(sqlTableCredentials);
            executeSqlStatement(sqlTableUser);
            executeSqlStatement(sqlTableGrades);
            executeSqlStatement(sqlTableStudents);
            executeSqlStatement(sqlTableTeacher);


            //insertUser(new Student(null, "S", "S", Role.STUDENT), "student");
            //insertUser(new Teacher(null, "T", "T", Role.TEACHER), "teacher");
            //insertUser(new Admin(null, "A", "A", Role.ADMIN), "admin");
            
            insertUser(new Student(null, "Jac", "Mei", Role.STUDENT), "mail");

            // subjets needs entire tree because javax.security.auth.Subject exists
            insertUser(new Teacher(null, "Teacher", "Test", Role.STUDENT, new src.main.java.classes.Subject[]{}), "teacher");

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

    public static ResultSet executeSqlSelectStatement(String sql) throws SQLException {

        Statement statement;
        statement = c.createStatement();
        return statement.executeQuery(sql);
    }

    public static User tryToLogin(String email, String password) throws SQLException {

        String sqlCred = "SELECT * FROM CREDENTIALS WHERE (EMAIL = '" + email + "' AND PASSWORD = '" + password + "');";
        ResultSet rsCred = executeSqlSelectStatement(sqlCred);

        if (rsCred.next()) {
            String id = String.valueOf(rsCred.getInt("UID"));
            rsCred.close();

            String sqlUser = "SELECT * FROM USER WHERE(UID = '" + id + "');";
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


        System.out.println(cred);

        System.out.println(data);

        return cred == 1 && data == 1;
    }


    /*
        TODO subject and schoolclass
    */
    public static boolean insertGrade(Grade grade, Student student, src.main.java.classes.Subject subject, String schoolclass) throws SQLException {

        PreparedStatement pSGrade;

        pSGrade = c.prepareStatement(sqlInsertGrade);

        pSGrade.setInt(1, student.getId());
        pSGrade.setString(2, schoolclass);
        pSGrade.setString(3, subject.name());
        pSGrade.setString(4, grade.getGrade_bez());
        pSGrade.setInt(5, grade.getGrade_val());


        int insertGrade = pSGrade.executeUpdate();

        return insertGrade == 1;
    }

    public static boolean addStudentToClass(int studetnId, String class_id) throws SQLException {

        PreparedStatement ps;

        ps = c.prepareStatement(sqlAddStudentToClass);

        ps.setInt(1, studetnId);
        ps.setString(2, class_id);

        int i = ps.executeUpdate();

        return i == 1;
    }

    public static boolean addTeacherWithSubjectToClass(int teacherId, String class_id, src.main.java.classes.Subject subject) throws SQLException {

        PreparedStatement ps;

        ps = c.prepareStatement(sqlAddTeacherWithSubjectToClass);

    

        ps.setInt(1, teacherId);
        ps.setString(2, class_id);
        ps.setString(3, subject.name());

        int i = ps.executeUpdate();

        return i == 1;}
/* 
    public static boolean deleteUser(int id) throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = c.prepareStatement(sqlDeleteUserCredentials);
        // set the corresponding param
        preparedStatement.setInt(1, id);
        // execute the delete statement
        preparedStatement = c.prepareStatement(sqlDeleteUserCredentials);
        // set the corresponding param
        preparedStatement.setInt(1, id);
        // execute the delete statement
        return preparedStatement.executeUpdate() == 2;
    }*/

    /*
        TODO finish Delete function
     */
    public static void deleteUser(int uid){

        String credentials = deleteString("CREDENTIALS", uid);
        String user = deleteString("USER", uid);
        String grades = deleteString("GRADES", uid);
        String students = deleteString("STUDENTS", uid);
        String teacher = deleteString("TEACHER", uid);



    }

    private static String deleteString(String table, int uid){
        return "DELETE FROM " + table + " WEHERE UID =" + uid + ";";
    }

}

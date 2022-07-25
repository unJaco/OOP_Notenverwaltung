package db;

import classes.*;


import java.sql.*;

public class DBHelper {

    static Connection c = null;

    static final String sqlTableCredentials = "CREATE TABLE IF NOT EXISTS CREDENTIALS " +
            "(UID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "EMAIL           TEXT    UNIQUE NOT NULL, " +
            " PASSWORD           TEXT    NOT NULL);";

    static final String sqlTableUser = "CREATE TABLE IF NOT EXISTS USER " +
            "(UID INTEGER UNIQUE NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " VORNAME           TEXT    NOT NULL, " +
            " ROLE            INT     NOT NULL);";

    static final String sqlTableGrades = "CREATE TABLE IF NOT EXISTS GRADES " +
            "(GRADE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "UID INT NOT NULL, " +
            "CLASS_ID TEXT NOT NULL," +
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

    static final String sqlInsertUserCredentials = "INSERT INTO CREDENTIALS(EMAIL, PASSWORD) VALUES(?,?);";

    static final String sqlInsertUserData = "INSERT INTO USER(UID, NAME, VORNAME, ROLE) VALUES(?,?,?,?);";

    static final String sqlInsertGrade = "INSERT INTO GRADES(UID, CLASS_ID, SUBJECT, GRADE_BEZ, GRADE_VAL) VALUES(?,?,?,?,?);";

    static final String sqlAddStudentToClass = "INSERT INTO STUDENTS (UID, CLASS_ID) VALUES(?,?);";

    static final String sqlAddTeacherWithSubjectToClass = "INSERT INTO TEACHER (UID, CLASS_ID, SUBJECT) VALUES(?,?, ?);";


    public static void connectToDb() throws SQLException {

        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:NOTENVERWALTUNG.db");

        /*
            executeSqlStatement("DROP TABLE GRADES");
            executeSqlStatement("DROP TABLE USER");
            executeSqlStatement("DROP TABLE CREDENTIALS");
            executeSqlStatement("DROP TABLE STUDENTS");
            executeSqlStatement("DROP TABLE TEACHER");
        */

            executeSqlStatement(sqlTableCredentials);
            executeSqlStatement(sqlTableUser);
            executeSqlStatement(sqlTableGrades);
            executeSqlStatement(sqlTableStudents);
            executeSqlStatement(sqlTableTeacher);
        /*
            insertUser(new Student(null, "S", "S", Role.STUDENT), "student", "7B");
            insertUser(new Teacher(null, "T", "T", Role.TEACHER), "teacher", null);
            insertUser(new Admin(null, "A", "A", Role.ADMIN), "admin", null);
        */

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

    public static boolean insertUser(User user, String email, String class_id) throws SQLException {

        PreparedStatement pSCredentials;
        PreparedStatement pSUserData;


        pSCredentials = c.prepareStatement(sqlInsertUserCredentials);
        pSUserData = c.prepareStatement(sqlInsertUserData);

        pSCredentials.setString(1, email);
        pSCredentials.setString(2, user.getFirstname() + "." + user.getLastname());

        boolean credentialsSuccess = pSCredentials.execute();

        pSCredentials.close();

        ResultSet rs = executeSqlSelectStatement("SELECT * FROM CREDENTIALS WHERE (EMAIL = '" + email + "');");
        int uid = rs.getInt("UID");

        user.setId(uid);

        pSUserData.setInt(1, user.getId());
        pSUserData.setString(2, user.getFirstname());
        pSUserData.setString(3, user.getLastname());
        pSUserData.setString(4, user.getRole().name());


        pSUserData.execute();

        boolean userSpecificSuccess = true;

        switch (user.getRole()){

            case STUDENT -> {
                userSpecificSuccess = addStudentToClass((Student) user, class_id);
            }
            case TEACHER -> {
                System.out.println("TEACHER");
            }
        }

        /*
            TODO low importance - both bools are false lol
         */
        return userSpecificSuccess && credentialsSuccess;
    }

    private static boolean addStudentToClass(Student student, String class_id) throws SQLException {

        PreparedStatement pSAddStudentToClass;
        pSAddStudentToClass = c.prepareStatement(sqlAddStudentToClass);

        pSAddStudentToClass.setInt(1, student.getId());
        pSAddStudentToClass.setString(2, class_id);

        boolean studentToClassSuccess = pSAddStudentToClass.execute();
        pSAddStudentToClass.close();

        return studentToClassSuccess;
    }

    /*
        TODO high important - add Teacher to a class with Subject
     */

    private static boolean addTeacherToClass(Teacher teacher) throws SQLException {

        PreparedStatement pSAddTeacherToClass;
        pSAddTeacherToClass = c.prepareStatement(sqlAddTeacherWithSubjectToClass);

        return false;
    }

    /*
        TODO subject and class_id
    */
    public static boolean insertGrade(Grade grade, Student student, Subject subject, String class_id) throws SQLException {

        PreparedStatement pSGrade;

        pSGrade = c.prepareStatement(sqlInsertGrade);

        pSGrade.setInt(1, student.getId());
        pSGrade.setString(2, class_id);
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

    public static boolean addTeacherWithSubjectToClass(int teacherId, String class_id, Subject subject) throws SQLException {

        PreparedStatement ps;

        ps = c.prepareStatement(sqlAddTeacherWithSubjectToClass);

        ps.setInt(1, teacherId);
        ps.setString(2, class_id);
        ps.setString(3, subject.name());

        int i = ps.executeUpdate();

        return i == 1;
    }

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

    /*public static boolean deleteUser(String email) throws SQLException {


        PreparedStatement preparedStatement;
        preparedStatement = c.prepareStatement(sqlDeleteUser);
        // set the corresponding param
        preparedStatement.setString(1, email);
        // execute the delete statement
        return preparedStatement.executeUpdate() == 1;
    }*/
}

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
            "(SIC_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UID INT NOT NULL," +
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


            executeSqlStatement(sqlTableCredentials);
            executeSqlStatement(sqlTableUser);
            executeSqlStatement(sqlTableGrades);
            executeSqlStatement(sqlTableStudents);
            executeSqlStatement(sqlTableTeacher);

        } catch (Exception e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            c.close();
            System.exit(0);

        }

    }

    public static boolean executeSqlStatement(String sql) throws SQLException {

        Statement statement;
        statement = c.createStatement();
        return statement.execute(sql);
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
            rsCred.close();
            return null;
        }
    }

    public static void insertUser(User user, String email, String classId) throws SQLException {

        PreparedStatement pSCredentials;
        PreparedStatement pSUserData;


        pSCredentials = c.prepareStatement(sqlInsertUserCredentials);
        pSUserData = c.prepareStatement(sqlInsertUserData);

        pSCredentials.setString(1, email);
        pSCredentials.setString(2, user.getFirstname() + "." + user.getLastname());

        pSCredentials.execute();

        pSCredentials.close();

        ResultSet rs = executeSqlSelectStatement("SELECT * FROM CREDENTIALS WHERE (EMAIL = '" + email + "');");
        int uid = rs.getInt("UID");

        user.setId(uid);

        pSUserData.setInt(1, user.getId());
        pSUserData.setString(2, user.getFirstname());
        pSUserData.setString(3, user.getLastname());
        pSUserData.setString(4, user.getRole().name());


        pSUserData.execute();


        if (user.getRole() == Role.STUDENT) addStudentToClass((Student) user, classId);
    }


    public static User getUser(int uid) throws SQLException {

        String sql = "SELECT * FROM USER WHERE (UID = '" + uid + "');";

        ResultSet rs = executeSqlSelectStatement(sql);

        Role role = Role.valueOf(rs.getString("ROLE"));

        switch (role){

            case STUDENT -> {return new Student(rs);}

            case TEACHER -> {return new Teacher(rs);}

            case ADMIN -> {return new Admin(rs);}

            default -> {return null;}
        }
    }

    public static int getIDEmail(String email) throws SQLException {

    String sql = "SELECT * FROM CREDENTIALS WHERE (EMAIL = '" + email + "');";

    ResultSet rs = executeSqlSelectStatement(sql);

    return rs.getInt("UID");

    }


    public static String getEMail(int uid) throws SQLException {
        String sql = "SELECT * FROM CREDENTIALS WHERE (UID = '" + uid + "');";

        return executeSqlSelectStatement(sql).getString("EMAIL");
    }

    private static boolean addStudentToClass(Student student, String classId) throws SQLException {

        PreparedStatement pSAddStudentToClass;
        pSAddStudentToClass = c.prepareStatement(sqlAddStudentToClass);

        pSAddStudentToClass.setInt(1, student.getId());
        pSAddStudentToClass.setString(2, classId);

        boolean studentToClassSuccess = pSAddStudentToClass.execute();
        pSAddStudentToClass.close();

        return studentToClassSuccess;
    }

    public static boolean insertGrade(String classId, int studentID,  Grade grade) throws SQLException {

        PreparedStatement pSGrade;

        pSGrade = c.prepareStatement(sqlInsertGrade);

        pSGrade.setInt(1, studentID);
        pSGrade.setString(2, classId);
        pSGrade.setString(3, grade.getSubject().name());
        pSGrade.setString(4, grade.getGradeBez());
        pSGrade.setInt(5, grade.getGradeVal());


        int insertGrade = pSGrade.executeUpdate();

        return insertGrade == 1;
    }

    public static boolean deleteGrade(int gradeId, int uid) throws SQLException {
        String sql = "DELETE FROM GRADES WHERE (GRADE_ID = '" + gradeId + "' AND UID = '" + uid + "');";
        return executeSqlStatement(sql);
    }

    public static boolean updateGradeVal(Grade grade) throws SQLException {

        String sql = "UPDATE GRADES SET GRADE_VAL = '" + grade.getGradeVal() + "' WHERE GRADE_ID = '" + grade.getGradeId() + "';";
        return executeSqlStatement(sql);
    }

    public static boolean updateGradeBez(Grade grade) throws SQLException {

        String sql = "UPDATE GRADES SET GRADE_BEZ = '" + grade.getGradeBez() + "' WHERE GRADE_ID = '" + grade.getGradeId() + "';";
        return executeSqlStatement(sql);
    }

    public static boolean addTeacherWithSubjectToClass(int teacherId, String classId, Subject subject) throws SQLException {

        PreparedStatement pS;

        pS = c.prepareStatement(sqlAddTeacherWithSubjectToClass);

        pS.setInt(1, teacherId);
        pS.setString(2, classId);
        pS.setString(3, subject.name());

        int i = pS.executeUpdate();
        pS.close();

        return i == 1;
    }

    public static boolean updateTeacherSubject(SubjectInClass subjectInClass) throws SQLException {
        String sql = "UPDATE TEACHER SET SUBJECT = '" + subjectInClass.getSubject() + "' WHERE '" + subjectInClass.getId() + "';";
        return executeSqlStatement(sql);
    }

    public static boolean updateTeacherClass(SubjectInClass subjectInClass) throws SQLException {
        String sql = "UPDATE TEACHER SET CLASS_ID = '" + subjectInClass.getClassId() + "' WHERE '" + subjectInClass.getId() + "';";
        return executeSqlStatement(sql);
    }

    public static boolean deleteTeacherEntry(int sicId, int uid) throws SQLException {
        String sql = "DELETE FROM TEACHER WHERE (SIC_ID = '" + sicId + "' AND UID = '" + uid + "');";
        return executeSqlStatement(sql);
    }

    public static void deleteUser(int uid) throws SQLException {

        String credentials = deleteString("CREDENTIALS", uid);
        String user = deleteString("USER", uid);
        String grades = deleteString("GRADES", uid);
        String students = deleteString("STUDENTS", uid);
        String teacher = deleteString("TEACHER", uid);

        executeSqlStatement(credentials);
        executeSqlStatement(user);
        executeSqlStatement(grades);
        executeSqlStatement(students);
        executeSqlStatement(teacher);

    }

    private static String deleteString(String table, int uid){
        return "DELETE FROM " + table + " WHERE UID = '" + uid + "';";
    }

    public static boolean changeCredentials(String email, String password) throws SQLException {
        String sql = "UPDATE CREDENTIALS SET PASSWORD = '" + password + "' WHERE email = '" + email + "';";
        return executeSqlStatement(sql);
    }
}

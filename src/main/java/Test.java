package src.main.java;

import src.main.java.classes.Admin;
import src.main.java.classes.Role;
import src.main.java.classes.User;


public class Test {
    public static void main(String[] args) {

        User user = new Admin(1, "Jacob", "M", Role.ADMIN);
    }
}

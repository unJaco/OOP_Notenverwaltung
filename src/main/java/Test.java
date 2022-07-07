import classes.Admin;
import classes.Role;
import classes.User;


public class Test {
    public static void main(String[] args) {

        User user = new Admin(1, "Jacob", "M", "t@t.de", Role.ADMIN);
    }
}

package classes;
import java.sql.SQLException;

public interface OnLogin {
    void onlogin(User user) throws SQLException;
}

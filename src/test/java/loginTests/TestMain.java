package loginTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class TestMain extends GuiTest{

    @Override
protected Parent getRootNode() {
  Parent parent = null;
  try {
    parent = FXMLLoader.load(getClass().getResource("/resources/javafx/login-view.fxml"));
  } catch (IOException ex) {
    ex.printStackTrace();
  }
  return parent;
}
    

    @Test
    public void falseLogin(){
      TextField mailfField = find("#mailField");
      TextField passField = find("#passField");
      Button loginButton = find("#loginButton");
      

      click(loginButton);

      Label loginLabel = find("#loginLabel");
      assertTrue(loginLabel.getText().equals("Anmeldedaten nicht korrekt!"));
    }
}
package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMainController extends CustomerMainController{
    @FXML
    private Text username;
    @FXML
    private Text userid;
    @FXML
    private Text myrole;

    public void setTextElements(String nameText, String userIdText, String myroleText) {
        username.setText(nameText);
        userid.setText("ID: " + userIdText);
        myrole.setText("ROLE: "+ myroleText);
    }

    @FXML
    public void handleLogOutButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        CustomerUser.logout();
        stage.setScene(scene);
    }
}

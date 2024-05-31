package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerMyPageController extends CustomerMainController{


    @FXML
    private void handleLogOutButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
}

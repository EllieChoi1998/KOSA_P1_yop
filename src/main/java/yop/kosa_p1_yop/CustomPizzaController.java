package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomPizzaController extends CustomerMainController{
    @FXML
    private void handleCreateNewCustomButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DowAndSizeAndsauce.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
    @FXML
    private void handleAddToppingButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ToppingSelection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
}

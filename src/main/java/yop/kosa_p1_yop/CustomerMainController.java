package yop.kosa_p1_yop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CustomerMainController {


    @FXML
    public void handleMyPageButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPage.fxml"));
        Parent root = fxmlLoader.load();
        CustomerMyPageController controller = fxmlLoader.getController();
        controller.setTextElements(CustomerUser.getName(), CustomerUser.getId(), CustomerUser.getCredits() );
        Scene scene = new Scene(root, 450, 820);
        stage.setScene(scene);
    }

    /*
    Handler for going back to the CustomerMainPage by clicking invisible button at the top of the interface.
     */
    @FXML
    private void handleInvisibleButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    @FXML
    private void handleOrderButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppOrderStandard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    @FXML
    private void handleMyPizzasButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPizzas.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        // 현재 창을 닫기
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }


}

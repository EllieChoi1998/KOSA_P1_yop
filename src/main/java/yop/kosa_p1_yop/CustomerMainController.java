package yop.kosa_p1_yop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CustomerMainController {


    @FXML
    private void handleMyPageButtonAction() throws IOException {
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerMain.fxml"));
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    @FXML
    private void popup(ActionEvent event) {
        try {
            // calories.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppCaloriesPage.fxml"));
            Parent root = loader.load();

            // 현재의 스테이지 얻기
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // 타이틀 바 제거
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

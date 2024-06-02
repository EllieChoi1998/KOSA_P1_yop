package yop.kosa_p1_yop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.text.Text;

import java.io.IOException;

public class CustomerMyPageController extends CustomerMainController{
    @FXML
    private Text username;
    @FXML
    private Text userid;
    @FXML
    private Text credits;

    public void setTextElements(String nameText, String userIdText, double creditsText) {
        username.setText(nameText);
        userid.setText(userIdText);
        credits.setText(String.valueOf(creditsText));
    }

    @FXML
    public void handleLogOutButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        CustomerUser.logout();
        stage.setScene(scene);
    }

    @FXML
    public void handleSignOutButtonAction() {
        System.out.println("Sign out button clicked");
        try {
            // Perform the signout operation
            System.out.println("Attempting to sign out");
            CustomerUser.signout();
            System.out.println("Sign out completed");

            // Load the new scene
            System.out.println("Loading new scene");
            Stage stage = (Stage) AppMain.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 820);
            stage.setScene(scene);
            System.out.println("Scene changed successfully");
        } catch (IOException e) {
            e.printStackTrace();  // Print stack trace to console
        }
    }



    @FXML
    private void handleOrderHistoryButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppOrderHistory.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    @FXML
    private void handleMoveToStandardButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppOrderStandard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    @FXML
    private void handleMoveToCustomButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppOrderCustom.fxml"));
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

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        // 현재 창을 닫기
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleMyPizzasButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPizzas.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
    @FXML
    private void handleCreateNewCustomButtonAction() throws IOException{
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


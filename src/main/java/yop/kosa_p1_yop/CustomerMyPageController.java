package yop.kosa_p1_yop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.fxml.*;

import java.util.Optional;

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
    private void handleCurrentOrderButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Customer_CurrentOrder.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
    @FXML
    public void handleLogOutButtonAction() throws IOException {

        if(!CustomerUser.getBucket().isEmpty()) {
            // Create the alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("로그아웃 확인");
            alert.setHeaderText(null);
            alert.setContentText("지금 로그아웃하면, 장바구니가 초기화 됩니다.\n그래도 로그아웃 하시겠습니까?");

            // Create custom buttons
            ButtonType buttonTypeYes = new ButtonType("네", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeNo = new ButtonType("아니오", ButtonBar.ButtonData.CANCEL_CLOSE);

            // Set the buttons to the alert
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Get the buttons from the alert dialog pane
            Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
            Button noButton = (Button) alert.getDialogPane().lookupButton(buttonTypeNo);

            // Create an HBox to hold the buttons
            HBox buttonBox = new HBox(10); // 10 is the spacing between buttons
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(yesButton, noButton);

            // Set the HBox as the expandable content of the alert
            alert.getDialogPane().setExpandableContent(buttonBox);
            alert.getDialogPane().setExpanded(true);

            // Optionally, add detailed information to the expandable content
            Label details = new Label("여기에 추가 정보를 입력하세요. 장바구니가 초기화되는 이유 등에 대한 정보를 제공할 수 있습니다.");
            VBox vBox = new VBox();
            vBox.getChildren().addAll(details, buttonBox);
            alert.getDialogPane().setExpandableContent(vBox);

            // Show the dialog and capture the response
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                // Perform logout
                CustomerUser.logout();

                // Load the main scene
                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                stage.setScene(scene);
            }
        } else {
            CustomerUser.logout();

            // Load the main scene
            Stage stage = (Stage) AppMain.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 820);
            stage.setScene(scene);
        }
    }

    @FXML
    public void handleSignOutButtonAction() {
        System.out.println("Sign out button clicked");

        // Create the alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("회원 탈퇴 확인");
        alert.setHeaderText(null);
        alert.setContentText("회원 탈퇴는 되돌릴 수 없습니다.\n그래도 진행 하시겠습니까?");

        // Create custom buttons
        ButtonType buttonTypeYes = new ButtonType("네", ButtonType.OK.getButtonData());
        ButtonType buttonTypeNo = new ButtonType("아니오", ButtonType.CANCEL.getButtonData());

        // Set the buttons to the alert
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Show the dialog and capture the response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
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
    private void handleChangePasswordButtonAction() throws IOException {
        // Create a TextInputDialog for the user to enter the new password
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your new password:");

        // Show the dialog and wait for user input
        String result = dialog.showAndWait().orElse("");

        // Process the user input
        if (!result.isEmpty()) {
            // Handle the new password, e.g., update it in your system
            CustomerUser.changepwdOrname(CustomerUser.getId(), CustomerUser.getPwd(), result, 1);

            // Display a message or perform any other actions as needed
            System.out.println("New Password: " + result);

            // Optionally, you can show another dialog or perform any other actions here
            showPasswordChangedDialog();
        }
    }

    @FXML
    private void handleChangeNameButtonAction() throws IOException {
        // Create a TextInputDialog for the user to enter the new password
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change User Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter your new user name:");

        // Show the dialog and wait for user input
        String result = dialog.showAndWait().orElse("");

        // Process the user input
        if (!result.isEmpty()) {
            // Handle the new password, e.g., update it in your system
            CustomerUser.changepwdOrname(CustomerUser.getId(), CustomerUser.getPwd(), result, 2);


            // Optionally, you can show another dialog or perform any other actions here
            showNameChangeDialog();
        }
    }

    // Method to show a dialog indicating that the password has been changed
    private void showPasswordChangedDialog() throws IOException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Changed");
        alert.setHeaderText(null);
        alert.setContentText("Password has been changed. Please log in again.");

        alert.showAndWait();
        handleLogOutButtonAction();
    }

    // Method to show a dialog indicating that the password has been changed
    private void showNameChangeDialog() throws IOException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Name Changed");
        alert.setHeaderText(null);
        alert.setContentText("User Name has been changed.\nYou can check on the MyPage.");

        alert.showAndWait();

        handleMyPageButtonAction();
    }

    @FXML
    private void handleBucketButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerBucket.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
}


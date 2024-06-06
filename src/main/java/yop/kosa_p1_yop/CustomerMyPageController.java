package yop.kosa_p1_yop;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.fxml.*;
import javafx.scene.control.TextInputDialog;
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

}


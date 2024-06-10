package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PizzaNamePopupController {

    @FXML
    private TextField pizzaNameTextField;

    @FXML
    private void handleConfirmButtonAction() {
        String pizzaName = pizzaNameTextField.getText();
        System.out.println("Entered pizza name: " + pizzaName);

        Stage stage = (Stage) pizzaNameTextField.getScene().getWindow();
        stage.close();
    }
}
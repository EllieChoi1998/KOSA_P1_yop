package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SizeSelectionController extends CustomerMainController{
    @FXML
    private TextField LsizeField;

    @FXML
    private TextField MsizeField;

    @FXML
    private void handleLsizePlus() {
        updateTextFieldValue(LsizeField, 1);
    }

    @FXML
    private void handleLsizeMinus() {
        updateTextFieldValue(LsizeField, -1);
    }

    @FXML
    private void handleMsizePlus() {
        updateTextFieldValue(MsizeField, 1);
    }

    @FXML
    private void handleMsizeMinus() {
        updateTextFieldValue(MsizeField, -1);
    }

    private void updateTextFieldValue(TextField textField, int delta) {
        try {
            int currentValue = Integer.parseInt(textField.getText());
            int newValue = currentValue + delta;
            if (newValue >= 0) { // 음수가 되지 않도록 제한
                textField.setText(String.valueOf(newValue));
            }
        } catch (NumberFormatException e) {
            textField.setText("0");
        }
    }

}


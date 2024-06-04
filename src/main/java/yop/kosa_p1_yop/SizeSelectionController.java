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
    @FXML
    private void sizeselectionpopup(ActionEvent event) {
        try {
            // calories.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppSizeSelectionPage.fxml"));
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


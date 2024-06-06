package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ToppingItemController extends CustomerMainController{
    @FXML
    private Text toppingName;
    @FXML
    private Button addButton;
    @FXML
    private TextField toppingcnt;
    @FXML
    private Button removeButton;

    private AddToppingsController addToppingsController;
    private boolean toppingAdded = false;

    public void setName(String name) {
        toppingName.setText(name);
    }
    public void initialize() {
        removeButton.setDisable(true); // 초기에는 - 버튼 비활성화
    }

    public void setAddToppingsController(AddToppingsController controller) {
        this.addToppingsController = controller;
    }

    @FXML
    private void handleAddTopping() {
        if (!toppingAdded) {
            updateTextFieldValue(toppingcnt, 1);
            addToppingsController.showToppingImage(toppingName.getText());
            addToppingsController.addTopping(toppingName.getText());
            toppingAdded = true;
            addButton.setDisable(true);
            removeButton.setDisable(false);
        }
    }

    @FXML
    private void handleRemoveTopping() {
        updateTextFieldValue(toppingcnt, -1);
        addToppingsController.hideToppingImage(toppingName.getText());
        addToppingsController.removeTopping(toppingName.getText());
        toppingAdded = false;
        addButton.setDisable(false);
        removeButton.setDisable(true);
    }

    @FXML
    private void Toppingpopup(ActionEvent event) {
        try {
            // calories.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ToppingCaloriesPage.fxml"));
            Parent root = loader.load();

            ToppingCaloriesPageController controller = loader.getController();
            controller.setToppingName(toppingName.getText());
            // 현재의 스테이지 얻기
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // 타이틀 바 제거
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

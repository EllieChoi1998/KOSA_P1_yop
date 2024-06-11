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

public class ToppingItemController extends CustomPizzaController{
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
    private boolean isLargeSize;


    public void setName(String name) {
        toppingName.setText(name);
    }
    public void setIsLargeSize(boolean isLargeSize) {
        this.isLargeSize = isLargeSize;
//        System.out.println("isLargeSize: " + isLargeSize);
    }

    public void setAddToppingsController(AddToppingsController controller) {
        this.addToppingsController = controller;
    }

    public void initialize() {
        setIsLargeSize(isLargeSize);
        removeButton.setDisable(true);
    }


    @FXML
    private void handleAddTopping() {
        if (!toppingAdded) {
            if (CustomPizza.addToppings(toppingName.getText())) {
                updateTextFieldValue(toppingcnt, 1); // 수량 증가
                addToppingsController.showToppingImage(toppingName.getText()); // UI에 토핑 이미지 표시
                addToppingsController.addTopping(toppingName.getText());
                toppingAdded = true;
                addButton.setDisable(true); // "추가" 버튼 비활성화
                removeButton.setDisable(false); // "제거" 버튼 활성화
                System.out.println("Topping added successfully in UI: " + toppingName.getText());
            } else {
                System.out.println("Failed to add topping in UI: " + toppingName.getText());
            }
        }
    }

    @FXML
    private void handleRemoveTopping() {
        if (CustomPizza.deleteToppings(toppingName.getText())) {
            updateTextFieldValue(toppingcnt, -1); // 수량 감소
            addToppingsController.hideToppingImage(toppingName.getText()); // UI에서 토핑 이미지 숨기기
            addToppingsController.removeTopping(toppingName.getText());
            toppingAdded = false;
            addButton.setDisable(false); // "추가" 버튼 활성화
            removeButton.setDisable(true); // "제거" 버튼 비활성화
            System.out.println("Topping removed successfully in UI: " + toppingName.getText());
        } else {
            System.out.println("Failed to remove topping in UI: " + toppingName.getText());
        }
    }

    @FXML
    private void Toppingpopup(ActionEvent event) {
        try {
            // calories.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ToppingCaloriesPage.fxml"));
            Parent root = loader.load();
            ToppingCaloriesPageController controller = loader.getController();
            controller.setToppingNameAndSize(toppingName.getText());
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

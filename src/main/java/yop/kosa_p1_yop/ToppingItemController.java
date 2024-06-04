package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

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


    public void setAddToppingsController(AddToppingsController controller) {
        this.addToppingsController = controller;
    }

    @FXML
    private void handleAddTopping() {
        if (!toppingAdded) {
            updateTextFieldValue(toppingcnt, 1);
            addToppingsController.showToppingImage(toppingName.getText());
            toppingAdded = true;
            addButton.setDisable(true);
        }
    }

    @FXML
    private void handleRemoveTopping() {
        updateTextFieldValue(toppingcnt, -1);
        addToppingsController.hideToppingImage(toppingName.getText());
        toppingAdded = false;
        addButton.setDisable(false);
    }

    @FXML
    private void popup() {
        // 영양 성분 팝업 로직 추가
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

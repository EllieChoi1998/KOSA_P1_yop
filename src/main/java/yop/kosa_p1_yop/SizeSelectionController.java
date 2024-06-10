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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SizeSelectionController extends StandardPizzaCaloriesPageController {
    @FXML
    private TextField LsizeField;

    @FXML
    private TextField MsizeField;

    @FXML
    private TextField LsizePrice;

    @FXML
    private TextField MsizePrice;
    @FXML
    private TextField TotalPrice;

    private String pizzaName;
    private int priceL;
    private int priceM;

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
        loadPizzaPrices();
    }

    private void loadPizzaPrices() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT pizza_size, price FROM pizza WHERE name = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, pizzaName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String size = rs.getString("pizza_size");
                int price = rs.getInt("price");

                if ("L".equals(size)) {
                    priceL = price;
                } else if ("M".equals(size)) {
                    priceM = price;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        updatePrices();
    }

    private void updatePrices() {
        try {
            int quantityL = Integer.parseInt(LsizeField.getText());
            int quantityM = Integer.parseInt(MsizeField.getText());
            int totalPriceL = priceL * quantityL;
            int totalPriceM = priceM * quantityM;
            int totalPrice = totalPriceM + totalPriceL;
            LsizePrice.setText(String.valueOf(totalPriceL));
            MsizePrice.setText(String.valueOf(totalPriceM));
            TotalPrice.setText(String.valueOf(totalPrice));
        } catch (NumberFormatException e) {
            LsizePrice.setText("0");
            MsizePrice.setText("0");
            TotalPrice.setText("0");
        }
    }

    @FXML
    private void handleLsizePlus() {
        updateTextFieldValue(LsizeField, 1);
        updatePrices();
    }

    @FXML
    private void handleLsizeMinus() {
        updateTextFieldValue(LsizeField, -1);
        updatePrices();
    }

    @FXML
    private void handleMsizePlus() {
        updateTextFieldValue(MsizeField, 1);
        updatePrices();
    }

    @FXML
    private void handleMsizeMinus() {
        updateTextFieldValue(MsizeField, -1);
        updatePrices();
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
    private void handleSideMenubtn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppSideMenuOrder.fxml"));
            Parent root = loader.load();

            // AppCaloriesPageController의 인스턴스 가져오기
            StandardPizzaCaloriesPageController controller = loader.getController();

            // 새로운 스테이지 생성
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // 타이틀 바 제거
            stage.setScene(new Scene(root));

            // 스테이지 표시
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
}

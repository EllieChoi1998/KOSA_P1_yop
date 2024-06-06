package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ToppingCaloriesPageController {

    @FXML
    private Text weight, calories, proteins, fats, sugars, salts, price;

    private String toppingName;

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
        loadToppingNutritionalInfo();
    }

    private void loadToppingNutritionalInfo() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT weight, calories, proteins, fats, sugars, salts, price FROM ingredient WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, toppingName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                weight.setText(rs.getString("weight") + "g");
                calories.setText(rs.getString("calories") + " kcal");
                proteins.setText(rs.getString("proteins") + " g");
                fats.setText(rs.getString("fats") + " g");
                sugars.setText(rs.getString("sugars") + " g");
                salts.setText(rs.getString("salts") + " mg");
                price.setText(rs.getString("price") + " 원");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }
    @FXML
    private void handleCloseButtonAction() {
        // 팝업 창 닫기
        Stage stage = (Stage) weight.getScene().getWindow();
        stage.close();
    }

}

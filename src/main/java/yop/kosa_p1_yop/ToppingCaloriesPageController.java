package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ToppingCaloriesPageController {

    @FXML
    private Text weight, calories, proteins, fats, sugars, salts;

    private String toppingName;
    private boolean isLargeSize;

    public void setToppingNameAndSize(String toppingName) {
        this.toppingName = toppingName;
        this.isLargeSize = CustomPizza.isLarge();
        loadToppingNutritionalInfo();
    }

    private void loadToppingNutritionalInfo() {

        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT weight, calories, proteins, fats, sugars, salts FROM ingredient WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, toppingName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                double sizeFactor = isLargeSize ? 1.5 : 1.0; // 사이즈에 따른 배수 설정

                weight.setText(Math.round(rs.getDouble("weight") * sizeFactor) + "g");
                calories.setText(Math.round(rs.getDouble("calories") * sizeFactor) + " kcal");
                proteins.setText(Math.round(rs.getDouble("proteins") * sizeFactor) + " g");
                fats.setText(Math.round(rs.getDouble("fats") * sizeFactor) + " g");
                sugars.setText(Math.round(rs.getDouble("sugars") * sizeFactor) + " g");
                salts.setText(Math.round(rs.getDouble("salts") * sizeFactor) + " mg");
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

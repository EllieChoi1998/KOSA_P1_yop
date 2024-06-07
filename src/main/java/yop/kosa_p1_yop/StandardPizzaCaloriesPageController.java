package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StandardPizzaCaloriesPageController extends CustomerMyPageController {
    @FXML
    private Text weight;
    @FXML
    private Text calories;
    @FXML
    private Text proteins;
    @FXML
    private Text fats;
    @FXML
    private Text salts;
    @FXML
    private Text sugars;
    @FXML
    private Text price;
    @FXML
    private Text pizzaname;
    private String pizzaName;

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
        loadPizzaNutritionalInfo();
    }

    @FXML
    private void handlePizzaButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String pizzaName = clickedButton.getText();
        popup(pizzaName); // 수정된 호출
    }

    private void loadPizzaNutritionalInfo() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT weight, calories, proteins, fats, sugars, salts, price FROM pizza WHERE name = ? AND pizza_size = 'M'";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, pizzaName);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                weight.setText(rs.getString("weight") + "g");
                calories.setText(rs.getString("calories") + " kcal");
                proteins.setText(rs.getString("proteins") + " g");
                fats.setText(rs.getString("fats") + " g");
                sugars.setText(rs.getString("sugars") + " g");
                salts.setText(rs.getString("salts") + " mg");
                price.setText(rs.getString("price") + " 원");

                pizzaname.setText(pizzaName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }

    @FXML
    private void popup(String pizzaName) {
        try {
            // AppCaloriesPage.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppCaloriesPage.fxml"));
            Parent root = loader.load();

            // AppCaloriesPageController의 인스턴스 가져오기
            StandardPizzaCaloriesPageController controller = loader.getController();

            // 피자 이름 설정
            controller.setPizzaName(pizzaName);

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
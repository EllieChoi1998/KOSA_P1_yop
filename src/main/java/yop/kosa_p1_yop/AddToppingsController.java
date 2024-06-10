package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddToppingsController extends CustomPizzaController{
    @FXML
    private VBox toppingsContainer;

    @FXML
    private ImageView pineappletopping, bacontopping, bulgogitopping, shrimptopping, oniontopping, pepperonitopping, capsicumtopping, hamtopping;
    @FXML
    private Text caloriesText, priceText;

    private int totalCalories ;
    private int totalPrice ;
    private boolean isLargeSize;

    public void setInitialValues(int initialCalories, int initialPrice, boolean isLargeSize) {
        totalCalories = initialCalories;
        totalPrice = initialPrice;
        this.isLargeSize = isLargeSize;
        System.out.println("AddToppingsController: isLargeSize set to " + isLargeSize);
        loadToppingsFromDatabase();
        updateTotalCaloriesAndPrice();
    }

    @FXML
    public void initialize() {
        setInitialValues(totalCalories, totalPrice, isLargeSize);
    }

    private void loadToppingsFromDatabase() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT id, name, calories, price FROM ingredient WHERE ingredient_type_id = 5";
            rs = DatabaseConnect.getSQLResult(conn, query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                addToppingToUI(id, name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }

    private void addToppingToUI(int id, String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ToppingItem.fxml"));
        AnchorPane toppingItem = loader.load();

        ToppingItemController controller = loader.getController();
        controller.setIsLargeSize(isLargeSize);
        System.out.println(isLargeSize);
        controller.setName(name);
        controller.setAddToppingsController(this);
        toppingsContainer.getChildren().add(toppingItem);
    }

    public void showToppingImage(String toppingName) {
        switch (toppingName) {
            case "파인애플":
                pineappletopping.setVisible(true);
                break;
            case "베이컨":
                bacontopping.setVisible(true);
                break;
            case "불고기":
                bulgogitopping.setVisible(true);
                break;
            case "꼬리알새우":
                shrimptopping.setVisible(true);
                break;
            case "양파":
                oniontopping.setVisible(true);
                break;
            case "페퍼로니":
                pepperonitopping.setVisible(true);
                break;
            case "피망":
                capsicumtopping.setVisible(true);
                break;
            case "햄":
                hamtopping.setVisible(true);
                break;
            default:
                break;
        }
    }

    public void hideToppingImage(String toppingName) {
        switch (toppingName) {
            case "파인애플":
                pineappletopping.setVisible(false);
                break;
            case "베이컨":
                bacontopping.setVisible(false);
                break;
            case "불고기":
                bulgogitopping.setVisible(false);
                break;
            case "꼬리알새우":
                shrimptopping.setVisible(false);
                break;
            case "양파":
                oniontopping.setVisible(false);
                break;
            case "페퍼로니":
                pepperonitopping.setVisible(false);
                break;
            case "피망":
                capsicumtopping.setVisible(false);
                break;
            case "햄":
                hamtopping.setVisible(false);
                break;
            default:
                break;
        }
    }
    public void addTopping(String toppingName) {
        String query = "SELECT calories, price FROM ingredient WHERE name = ?";
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, toppingName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int toppingCalories = rs.getInt("calories");
                int toppingPrice = rs.getInt("price");
                // 사이즈에 따라 가격과 칼로리 업데이트
                if (isLargeSize) {
                    toppingCalories *= 1.5;
                    toppingPrice *= 1.5;
                } else {
                    toppingCalories *= 1.0;
                    toppingPrice *= 1.0;
                }

                totalCalories += toppingCalories;
                totalPrice += toppingPrice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        updateTotalCaloriesAndPrice();
    }

    public void removeTopping(String toppingName) {
        String query = "SELECT calories, price FROM ingredient WHERE name = ?";
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, toppingName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int toppingCalories = rs.getInt("calories");
                int toppingPrice = rs.getInt("price");
                // 사이즈에 따라 가격과 칼로리 업데이트
                if (isLargeSize) {
                    toppingCalories *= 1.5;
                    toppingPrice *= 1.5;
                } else {
                    toppingCalories *= 1.0;
                    toppingPrice *= 1.0;
                }
                totalCalories -= toppingCalories;
                totalPrice -= toppingPrice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
        updateTotalCaloriesAndPrice();
    }

    private void updateTotalCaloriesAndPrice() {
        caloriesText.setText(String.valueOf(totalCalories));
        priceText.setText(String.valueOf(totalPrice));
    }

    @FXML
    private Button Backbtn;
    @FXML
    private void handleBackButtonAction() {
        // 현재 Stage 가져오기
        Stage stage = (Stage) Backbtn.getScene().getWindow();

        try {
            // 이전 Scene 로드 (여기서 이전 Scene의 FXML 파일 경로를 지정하세요)
            Scene previousScene = new Scene(FXMLLoader.load(getClass().getResource("DowAndSizeAndsauce.fxml")));

            // Stage에 이전 Scene 설정
            stage.setScene(previousScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button Orderbtn;

    @FXML
    private void handleOrderButtonAction() {
        // "완성하기" 버튼 클릭 시 팝업 창을 표시합니다.
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PizzaNamePopup.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("피자 이름 입력");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



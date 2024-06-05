package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddToppingsController extends CustomPizzaController {
    @FXML
    private VBox toppingsContainer;

    @FXML
    private ImageView pineappletopping, bacontopping, bulgogitopping, shrimptopping, oniontopping, pepperonitopping, capsicumtopping, hamtopping;
    @FXML
    private Text caloriesText;
    @FXML
    private Text priceText;

    private int totalCalories ;
    private int totalPrice ;

    @FXML
    public void initialize() {
        loadToppingsFromDatabase();
        updateTotalCaloriesAndPrice();
    }
    public void setInitialValues(int initialCalories, int initialPrice) {
        totalCalories = initialCalories;
        totalPrice = initialPrice;
        updateTotalCaloriesAndPrice();
    }

    private void loadToppingsFromDatabase() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT id, name, calories, price FROM ingredients WHERE type = 5";
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
        String query = "SELECT calories, price FROM ingredients WHERE name = ?";
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, toppingName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalCalories += rs.getInt("calories");
                totalPrice += rs.getInt("price");
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
        String query = "SELECT calories, price FROM ingredients WHERE name = ?";
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, toppingName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalCalories -= rs.getInt("calories");
                totalPrice -= rs.getInt("price");
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
}



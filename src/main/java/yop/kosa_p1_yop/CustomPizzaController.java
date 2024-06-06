package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.IOException;

public class CustomPizzaController extends CustomerMainController{
    @FXML
    private void handleCreateNewCustomButtonAction() throws IOException {
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DowAndSizeAndsauce.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
    @FXML
    private void handleAddToppingButtonAction() throws IOException{
        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ToppingSelection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);

        AddToppingsController controller = fxmlLoader.getController();
        controller.setInitialValues(Integer.parseInt(caloriesText.getText()), Integer.parseInt(priceText.getText()));
    }

    @FXML
    private RadioButton originalM, thinM, tomatosaucecheck, bulgogisaucecheck, sweatpotatocheck, cheeseedgecheck, mocheesecheck, checheesecheck;
    @FXML
    private Text caloriesText, priceText;

    @FXML
    public void initialize() {}

    @FXML
    private void updateSelectedIngredients() {
        int totalCalories = 0;
        int totalPrice = 0;

        if (originalM.isSelected()) {
            Ingredient ingredient = getIngredient("오리지널도우");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        } else if (thinM.isSelected()) {
            Ingredient ingredient = getIngredient("씬도우");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        }

        if (tomatosaucecheck.isSelected()) {
            Ingredient ingredient = getIngredient("토마토소스");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        } else if (bulgogisaucecheck.isSelected()) {
            Ingredient ingredient = getIngredient("불고기소스");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        }

        if (sweatpotatocheck.isSelected()) {
            Ingredient ingredient = getIngredient("고구마엣지");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        } else if (cheeseedgecheck.isSelected()) {
            Ingredient ingredient = getIngredient("치즈엣지");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        }

        if (mocheesecheck.isSelected()) {
            Ingredient ingredient = getIngredient("모짜렐라치즈");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        } else if (checheesecheck.isSelected()) {
            Ingredient ingredient = getIngredient("체더치즈");
            totalCalories += ingredient.getCalories();
            totalPrice += ingredient.getPrice();
        }

        caloriesText.setText(String.valueOf(totalCalories));
        priceText.setText(String.valueOf(totalPrice));
    }

    private Ingredient getIngredient(String name) {
        Connection conn = null;
        ResultSet rs = null;
        Ingredient ingredient = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT calories, price FROM ingredient WHERE name= ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int calories = rs.getInt("calories");
                int price = rs.getInt("price");
                ingredient = new Ingredient(calories, price);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }

        return ingredient;
    }

    private class Ingredient {
        private int calories;
        private int price;

        public Ingredient(int calories, int price) {
            this.calories = calories;
            this.price = price;
        }

        public int getCalories() {
            return calories;
        }

        public int getPrice() {
            return price;
        }
    }
}

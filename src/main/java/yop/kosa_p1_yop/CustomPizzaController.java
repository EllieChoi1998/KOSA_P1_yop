package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.IOException;
import java.sql.SQLException;

public class CustomPizzaController extends CustomerMainController{

    @FXML
    private void handleCreateNewCustomButtonAction() throws IOException {
        // 새로운 커스텀피자 만들기 누를 때마다 초기화
        CustomPizza.resetPizza();

        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DowAndSizeAndsauce.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }
    @FXML
    private void handleAddToppingButtonAction() throws IOException{
        // 선택된 재료들의 이름을 저장할 배열
        String[] base = new String[4];

        // Dow 라디오 버튼 선택 확인
        if (originalL.isSelected() || originalM.isSelected()) {
            base[0] = "o오리지널도우";
        } else {
            base[0] = "t씬도우";
        }

        // Sauce 라디오 버튼 선택 확인
        if (tomatosaucecheck.isSelected()) {
            base[1] = "to토마토소스";
        } else if (bulgogisaucecheck.isSelected()) {
            base[1] = "bul불고기소스";
        }

        // Edge 라디오 버튼 선택 확인
        if (sweatpotatocheck.isSelected()) {
            base[2] = "go고구마엣지";
        } else if (cheeseedgecheck.isSelected()) {
            base[2] = "ch치즈엣지";
        }

        // Cheese 라디오 버튼 선택 확인
        if (mocheesecheck.isSelected()) {
            base[3] = "mo모짜렐라치즈";
        } else if (checheesecheck.isSelected()) {
            base[3] = "ce체더치즈";
        }

        // CustomPizza 클래스의 createCustomPizza 메서드 호출하여 선택된 재료 전달
        CustomPizza.createCustomPizza(base, isLargeSize);

        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ToppingSelection.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);

        AddToppingsController controller = fxmlLoader.getController();
        controller.setInitialValues(
                Integer.parseInt(caloriesText.getText()),
                Integer.parseInt(priceText.getText()),
                isLargeSize
        );
    }
    protected boolean isLargeSize = false;
    @FXML
    private Text weight, calories, proteins, fats, salts, sugars;
    @FXML
    private RadioButton originalL, thinL, originalM, thinM, tomatosaucecheck, bulgogisaucecheck, sweatpotatocheck, cheeseedgecheck, mocheesecheck, checheesecheck;
    @FXML
    private Text caloriesText, priceText;
    @FXML
    private Text sweatpotatoedgeprice, cheeseedgeprice, mocheeseprice, checheeseprice;


    @FXML
    public void initialize() {
    }

    @FXML
    private void handleSizeRadioButtonAction(ActionEvent event) {
        if (originalL.isSelected() || thinL.isSelected()) {
            isLargeSize = true;
        } else {
            isLargeSize = false;

        }
//        System.out.print(isLargeSize);
        updateSelectedIngredients();
    }

    @FXML
    private void updateSelectedIngredients() {
        int totalCalories = 0;
        int totalPrice = 0;

        Ingredient selectedDough = null;

        // 도우 선택에 따른 처리
        if (originalM.isSelected()) {
            selectedDough = getIngredient("오리지널도우");
        } else if (thinM.isSelected()) {
            selectedDough = getIngredient("씬도우");
        } else if (originalL.isSelected()) {
            selectedDough = getIngredient("오리지널도우");
            isLargeSize = true;
        } else if (thinL.isSelected()) {
            selectedDough = getIngredient("씬도우");
            isLargeSize = true;
        }

        // 도우 선택 시 각 재료 가격 표시
        if (selectedDough != null) {
            updatePriceTextFields(isLargeSize);

            totalCalories += isLargeSize ? selectedDough.getCalories() * 1.5 : selectedDough.getCalories();
            totalPrice += isLargeSize ? selectedDough.getPrice() * 1.5 : selectedDough.getPrice();
        }

        if (tomatosaucecheck.isSelected()) {
            Ingredient ingredient = getIngredient("토마토소스");
            totalCalories += isLargeSize ? ingredient.getCalories() * 1.5 : ingredient.getCalories();
            // isLargeSize가 True 이면 isLargeSize ? ingredient.getCalories() * 1.5 반환
            totalPrice += isLargeSize ? ingredient.getPrice() * 1.5 : ingredient.getPrice();
        } else if (bulgogisaucecheck.isSelected()) {
            Ingredient ingredient = getIngredient("불고기소스");
            totalCalories += isLargeSize ? ingredient.getCalories() * 1.5 : ingredient.getCalories();
            totalPrice += isLargeSize ? ingredient.getPrice() * 1.5 : ingredient.getPrice();
        }

        if (sweatpotatocheck.isSelected()) {
            Ingredient ingredient = getIngredient("고구마엣지");
            totalCalories += isLargeSize ? ingredient.getCalories() * 1.5 : ingredient.getCalories();
            totalPrice += isLargeSize ? ingredient.getPrice() * 1.5 : ingredient.getPrice();
        } else if (cheeseedgecheck.isSelected()) {
            Ingredient ingredient = getIngredient("치즈엣지");
            totalCalories += isLargeSize ? ingredient.getCalories() * 1.5 : ingredient.getCalories();
            totalPrice += isLargeSize ? ingredient.getPrice() * 1.5 : ingredient.getPrice();
        }

        if (mocheesecheck.isSelected()) {
            Ingredient ingredient = getIngredient("모짜렐라치즈");
            totalCalories += isLargeSize ? ingredient.getCalories() * 1.5 : ingredient.getCalories();
            totalPrice += isLargeSize ? ingredient.getPrice() * 1.5 : ingredient.getPrice();
        } else if (checheesecheck.isSelected()) {
            Ingredient ingredient = getIngredient("체더치즈");
            totalCalories += isLargeSize ? ingredient.getCalories() * 1.5 : ingredient.getCalories();
            totalPrice += isLargeSize ? ingredient.getPrice() * 1.5 : ingredient.getPrice();
        }

        caloriesText.setText(String.valueOf(totalCalories));
        priceText.setText(String.valueOf(totalPrice));
    }

    private void updatePriceTextFields(boolean isLargeSize) {
        double sizeFactor = isLargeSize ? 1.5 : 1.0;

        Ingredient sweatpotatoedge = getIngredient("고구마엣지");
        sweatpotatoedgeprice.setText(Math.round(sweatpotatoedge.getPrice() * sizeFactor) + "원");

        Ingredient cheeseedge = getIngredient("치즈엣지");
        cheeseedgeprice.setText(Math.round(cheeseedge.getPrice() * sizeFactor) + "원");

        Ingredient mocheese = getIngredient("모짜렐라치즈");
        mocheeseprice.setText(Math.round(mocheese.getPrice() * sizeFactor) + "원");

        Ingredient checheese = getIngredient("체더치즈");
        checheeseprice.setText(Math.round(checheese.getPrice() * sizeFactor) + "원");
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

    @FXML
    private Button cheesebtn, sweatpobtn, bulgogibtn, tomatobtn, lsizebtn, msizebtn, mocheesebtn, checheesebtn;

    private String ingredientName;

    public void setIngredientNameAndSize(String ingredientName, boolean isLargeSize) {
        this.ingredientName = ingredientName;
        this.isLargeSize = isLargeSize;
        loadNutritionalInfo();
    }

    @FXML
    private void Basepopup(ActionEvent event) {
        try {
            // 현재 버튼의 id를 통해 재료 이름 가져오기
            Button sourceButton = (Button) event.getSource();
            String ingredientName = getIngredientName(sourceButton);

            // calories.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BaseCaloriesPage.fxml"));
            Parent root = loader.load();

            // 팝업 창 컨트롤러 가져오기
            CustomPizzaController controller = loader.getController();
            controller.setIngredientNameAndSize(ingredientName, isLargeSize); // 재료 이름과 사이즈 설정

            // 현재의 스테이지 얻기
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // 타이틀 바 제거
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getIngredientName(Button button) {
        if (button == cheesebtn) {
            return "치즈엣지";
        } else if (button == sweatpobtn) {
            return "고구마엣지";
        } else if (button == bulgogibtn) {
            return "불고기소스";
        } else if (button == tomatobtn) {
            return "토마토소스";
        } else if (button == lsizebtn) {
            return "씬도우";
        } else if (button == msizebtn) {
            return "오리지널도우";
        } else if (button == mocheesebtn) {
            return "모짜렐라치즈";
        } else if (button == checheesebtn) {
            return "체더치즈";
        }
        return null;
    }

    private void loadNutritionalInfo() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT weight, calories, proteins, fats, sugars, salts FROM ingredient WHERE name = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, ingredientName);
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

}

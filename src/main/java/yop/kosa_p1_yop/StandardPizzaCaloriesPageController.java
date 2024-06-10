package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StandardPizzaCaloriesPageController extends CustomerMyPageController {
    @FXML
    private Text weightM, caloriesM, proteinsM, fatsM, saltsM, sugarsM;
    @FXML
    private Text weightL, caloriesL, proteinsL, fatsL, saltsL, sugarsL;

    private String pizzaName;


    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
        loadPizzaNutritionalInfo();
    }

    @FXML
    private AnchorPane pizzaPane;


    @FXML
    public void initialize() {

        loadPizzaData();
    }

    private void loadPizzaData() {
        Connection conn = null;
        ResultSet rs = null;
        Map<String, Boolean> checker = new HashMap<>();

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin"); // 데이터베이스 연결
            String query = "SELECT name, pizza_size, price FROM pizza";

            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            int row = 0;
            int col = 0;
            int cellWidth = 200;
            int cellHeight = 200;
            double initialX = 14.0;
            double initialY = 14.0;

            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("pizza_size");
                int price = rs.getInt("price");

                if (!checker.containsKey(name)) {
                    checker.put(name, true);

                    // 이미지 뷰 생성 및 위치 설정
                    ImageView imageView = new ImageView();
                    String imageFileName = name + ".png"; // 한국어 이름을 그대로 사용하여 파일명 생성
                    File file = new File(System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Images/standard_pizzas/" + imageFileName);
                    try {
                        URL imageUrl = file.toURI().toURL();
                        Image image = new Image(imageUrl.toString());
                        imageView.setImage(image);
                        imageView.setFitHeight(250);
                        imageView.setFitWidth(250);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageView.setFitHeight(cellHeight);
                    imageView.setFitWidth(cellWidth);
                    imageView.setLayoutX(initialX + col * (cellWidth + 10));
                    imageView.setLayoutY(initialY + row * (cellHeight + 10));
                    pizzaPane.getChildren().add(imageView);

                    // 버튼 생성
                    Button button = new Button(name);
                    button.setOpacity(0); // 버튼을 투명하게 설정
                    button.setPrefHeight(200);
                    button.setPrefWidth(200);
                    button.setLayoutX(initialX + col * (cellWidth + 10));
                    button.setLayoutY(initialY + row * (cellHeight + 10));
                    button.setOnAction(e -> popup(name));
                    pizzaPane.getChildren().add(button);

                    Text nameText = new Text(name);
                    nameText.setX(initialX + col * (cellWidth + 10) + (cellWidth - nameText.getLayoutBounds().getWidth()) / 2);
                    nameText.setY(initialY + row * (cellHeight + 10) + cellHeight + 20);
                    pizzaPane.getChildren().add(nameText);

                    Text lSizePriceText = new Text("L: " + price + "원");
                    lSizePriceText.setX(initialX + col * (cellWidth + 10) + (cellWidth - lSizePriceText.getLayoutBounds().getWidth()) / 2);
                    lSizePriceText.setY(initialY + row * (cellHeight + 10) + cellHeight + 40);
                    pizzaPane.getChildren().add(lSizePriceText);

                    // M 사이즈 가격
                    int mSizePrice = 0; // Default value if not found
                    if (size.equals("L")) {
                        if (rs.next()) {
                            mSizePrice = rs.getInt("price");
                        }
                    } else if (size.equals("M")) {
                        mSizePrice = price;
                        if (rs.previous()) {
                            price = rs.getInt("price");
                        }
                    }
                    Text mSizePriceText = new Text("M: " + mSizePrice + "원");
                    mSizePriceText.setX(initialX + col * (cellWidth + 10));
                    mSizePriceText.setY(initialY + row * (cellHeight + 10) + cellHeight + 40);
                    pizzaPane.getChildren().add(mSizePriceText);
                    // 셀 위치 업데이트
                    col++;
                    if (col >= 2) { // 한 행에 2개씩 표시
                        col = 0;
                        row++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }



    private void loadPizzaNutritionalInfo() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT pizza_size, weight, calories, proteins, fats, sugars, salts FROM pizza WHERE name = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, pizzaName);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String size = rs.getString("pizza_size");
                String weightText = rs.getString("weight") + "g";
                String caloriesText = rs.getString("calories") + " kcal";
                String proteinsText = rs.getString("proteins") + " g";
                String fatsText = rs.getString("fats") + " g";
                String sugarsText = rs.getString("sugars") + " g";
                String saltsText = rs.getString("salts") + " mg";

                if ("M".equals(size)) {
                    weightM.setText(weightText);
                    caloriesM.setText(caloriesText);
                    proteinsM.setText(proteinsText);
                    fatsM.setText(fatsText);
                    sugarsM.setText(sugarsText);
                    saltsM.setText(saltsText);
                } else if ("L".equals(size)) {
                    weightL.setText(weightText);
                    caloriesL.setText(caloriesText);
                    proteinsL.setText(proteinsText);
                    fatsL.setText(fatsText);
                    sugarsL.setText(sugarsText);
                    saltsL.setText(saltsText);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }


    @FXML
    private void popup(String name) {
        try {
            // AppCaloriesPage.fxml 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppCaloriesPage.fxml"));
            Parent root = loader.load();

            // AppCaloriesPageController의 인스턴스 가져오기
            StandardPizzaCaloriesPageController controller = loader.getController();

            // 피자 이름 설정
            controller.setPizzaName(name);

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

            StandardPizzaCaloriesPageController controller = loader.getController();

            // 피자 이름 설정
            controller.setPizzaName(pizzaName);

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED); // 타이틀 바 제거
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
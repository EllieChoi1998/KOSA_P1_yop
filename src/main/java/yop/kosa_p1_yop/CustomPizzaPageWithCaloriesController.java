package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
import java.util.Set;

public class CustomPizzaPageWithCaloriesController extends CustomerMyPageController {
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
        Map<String, VBox> pizzaMap = new HashMap<>();

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin"); // 데이터베이스 연결
            String query = "SELECT id, name, pizza_size, price FROM pizza where customer_id ='"+CustomerUser.getId()+"'";

            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            int row = 0;
            int col = 0;
            int cellWidth = 200;
            int cellHeight = 200;
            double initialX = 14.0;
            double initialY = 14.0;
            double rowGap = 100.0; // 추가 간격

            while (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("pizza_size");
                int price = rs.getInt("price");
                int id = rs.getInt("id");

                VBox pizzaBox;

                // Check if the pizza has already been added
                if (!pizzaMap.containsKey(name)) {
                    pizzaBox = new VBox();
                    pizzaBox.setLayoutX(initialX + col * (cellWidth + 10));
                    pizzaBox.setLayoutY(initialY + row * (cellHeight + rowGap));
                    pizzaBox.setSpacing(10);

                    // 이미지 뷰 생성 및 위치 설정
                    ImageView imageView = new ImageView();
                    String imageFileName = id + ".png"; // 한국어 이름을 그대로 사용하여 파일명 생성
                    File file = new File(System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Custom_Pizzas/" + imageFileName);
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
                    pizzaBox.getChildren().add(imageView);

                    // 버튼 생성
                    Button button = new Button();
                    button.setOpacity(0); // 버튼을 투명하게 설정
                    button.setPrefHeight(cellHeight);
                    button.setPrefWidth(cellWidth);
                    button.setOnAction(e -> popup(name));

                    // StackPane에 이미지와 버튼을 추가하여 겹치지 않도록 배치
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(imageView, button);
                    pizzaBox.getChildren().add(stackPane);

                    // 피자 이름 텍스트
                    Text nameText = new Text(name);
                    nameText.setStyle("-fx-font-size: 20px;");
                    pizzaBox.getChildren().add(nameText);


                    // Add the VBox to the pizzaPane
                    pizzaPane.getChildren().add(pizzaBox);
                    pizzaMap.put(name, pizzaBox);

                    // 셀 위치 업데이트
                    col++;
                    if (col >= 2) { // 한 행에 2개씩 표시
                        col = 0;
                        row++;
                    }
                } else {
                    pizzaBox = pizzaMap.get(name);
                }

                // 각 사이즈별 박스 추가
                HBox sizeBox = createSizeBox(size + ": " + price + "원", id);
                pizzaBox.getChildren().add(sizeBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }

    private HBox createSizeBox(String labelText, int id) {
        HBox hBox = new HBox();

        Text label = new Text(labelText);
        Button minusButton = new Button("➖");
        minusButton.setStyle("-fx-background-color:   FFEEDD;");
        Text quantityText;
        Map<String, Map<Integer, Integer>> bucket = CustomerUser.getBucket();
        if (bucket.isEmpty() || bucket.get("Pizza").get(id) == null){
            quantityText = new Text("0");
        } else{
            quantityText = new Text(String.valueOf(bucket.get("Pizza").get(id)));
        }

        Button plusButton = new Button("➕");
        plusButton.setStyle("-fx-background-color:   FFEEDD;");

        minusButton.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityText.getText());
            if (quantity > 0) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                // Add action for minus button
                boolean result = CustomerUser.delete_from_bucket("Pizza", id); // 액션 추가
                if (result) {
                    Map<Integer, Integer> pizza = bucket.get("Pizza");
                    if (bucket.size() != 0 && pizza.size() != 0) {
                        Set<Integer> keys = pizza.keySet();
                        for (Integer key : keys) {
                            System.out.println(key + "\t" + pizza.get(key));
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("알림");
                    alert.setHeaderText(null);
                    alert.setContentText("장바구니에서 빠졌습니다.");

                    // Create the button and add a listener to close the alert
                    ButtonType okButton = new ButtonType("확인");
                    alert.getButtonTypes().setAll(okButton);

                    alert.showAndWait();
                }
            }
        });

        plusButton.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityText.getText());
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            // Add action for plus button
            boolean result = CustomerUser.add_to_bucket("Pizza", id); // 액션 추가
            if (result) {
                Map<Integer, Integer> pizza = bucket.get("Pizza");
                if (bucket.size() != 0 && pizza.size() != 0) {
                    Set<Integer> keys = pizza.keySet();
                    for (Integer key : keys) {
                        System.out.println(key + "\t" + pizza.get(key));
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("알림");
                alert.setHeaderText(null);
                alert.setContentText("장바구니에 추가 되었습니다.");

                ButtonType okButton = new ButtonType("확인");
                alert.getButtonTypes().setAll(okButton);

                alert.showAndWait();
            }
        });

        hBox.getChildren().addAll(label, minusButton, quantityText, plusButton);
        hBox.setSpacing(10); // Space between elements

        return hBox;
    }


    private void loadPizzaNutritionalInfo() {
        Connection conn = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin");
            String query = "SELECT pizza_size, weight, calories, proteins, fats, sugars, salts FROM pizza WHERE name = ? and customer_id = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, pizzaName);
            pstmt.setString(2, CustomerUser.getId());
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
            StandardPizzaPageWithCaloriesController controller = loader.getController();

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




}
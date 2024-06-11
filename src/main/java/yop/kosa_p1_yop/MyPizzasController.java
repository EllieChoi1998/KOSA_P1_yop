package yop.kosa_p1_yop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class MyPizzasController extends CustomPizzaController{
    @FXML
    private Pane pizzaPane;

    @FXML
    public void initialize() {
        loadPizzaData();
    }

    private void loadPizzaData() {
        Connection conn = null;
        ResultSet rs = null;
        Map<String, VBox> pizzaMap = new HashMap<>();

        double initialY = 10.0;
        double cellWidth = 450;
        double cellHeight = 450;

        try {
            conn = DatabaseConnect.serverConnect("pizza_admin", "admin"); // 데이터베이스 연결
            String query = "SELECT id, name, pizza_size, price FROM pizza WHERE customer_id = '" + CustomerUser.getId() + "'";

            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");

                VBox pizzaBox;


                // Check if the pizza has already been added
                if (!pizzaMap.containsKey(name)) {
                    pizzaBox = new VBox();
                    pizzaBox.setPrefWidth(450);
                    pizzaBox.setSpacing(5);

                    // 이미지 뷰 생성 및 위치 설정
                    ImageView imageView = new ImageView();
                    String imageFileName = id + ".png"; // 파일명 생성
                    File file = new File(System.getProperty("user.dir") + "/src/main/resources/yop/kosa_p1_yop/Custom_Pizzas/" + imageFileName);
                    try {
                        URL imageUrl = file.toURI().toURL();
                        Image image = new Image(imageUrl.toString());
                        imageView.setImage(image);
                        imageView.setFitHeight(350);
                        imageView.setFitWidth(350);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    pizzaBox.getChildren().add(imageView);
                    pizzaBox.setAlignment(Pos.CENTER);

                    VBox nameBox = new VBox();
                    nameBox.setSpacing(10);
                    nameBox.setAlignment(Pos.CENTER);

                    Text nameText = new Text(name);

                    nameText.setStyle("-fx-font-size: 24px;"); // 텍스트 크기 설정

                    Button deleteButton = new Button("삭제");
                    deleteButton.setStyle("-fx-font-size: 14px;  -fx-background-color: FEC88E;"); // 삭제 버튼 크기 설정
                    deleteButton.setOnAction(e -> remove(id));

                    nameBox.getChildren().addAll(nameText, deleteButton);
                    pizzaBox.getChildren().add(nameBox);

                    String tag = generate_tag(id);


                    Text tagText = new Text(tag);
                    tagText.setStyle("-fx-font-size: 18px;");
                    TextFlow textFlow = new TextFlow(tagText);
                    textFlow.setPrefWidth(200); // TextFlow의 가로 크기 설정 (텍스트 너비가 아닌 TextFlow의 너비)

                    pizzaBox.getChildren().add(textFlow);

                    pizzaPane.getChildren().add(pizzaBox); // pizzaPane에 피자 상자 추가
                    pizzaBox.setLayoutY(initialY); // y 좌표 설정
                    initialY += cellHeight + 35; // 다음 VBox의 y 좌표를 조정

                    pizzaMap.put(name, pizzaBox);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }
    }



    private void remove(Integer pizza_id) {
        // 삭제 메소드 구현
        System.out.println(pizza_id + " has been removed.");
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

    private static String generate_tag(int pizza_id) {
        String tag = "";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        String sql = "SELECT i.name " +
                "FROM ingredient i " +
                "JOIN ingredient_item ii ON i.id = ii.ing_id " +
                "WHERE ii.pizza_id = ? " +
                "ORDER BY i.id";

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pizza_id);
            rs = pstmt.executeQuery();

            // Collect ingredient names and concatenate them to the tag
            while (rs.next()) {
                tag += "#" + rs.getString("name") + " ";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);
        }

        return tag;
    }

}
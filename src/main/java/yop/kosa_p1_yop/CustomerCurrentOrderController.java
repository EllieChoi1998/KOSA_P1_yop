package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

public class CustomerCurrentOrderController extends CustomerMainController {
    @FXML
    private VBox orderInfoVBox;

    public void initialize() {
        Map<String, Map<Integer, Integer>> customer_current_order = CustomerUser.getCurrentOrder();
        if (!customer_current_order.isEmpty()) {
            FilltheScreen(customer_current_order);
        } else {
            Text noOrderText = new Text("There's no ongoing order. Please make an order!\uD83D\uDE4F");
            noOrderText.setStyle("-fx-font-size: 20px;");
            orderInfoVBox.getChildren().add(noOrderText);

            Button createOrderButton = new Button("Go to Create Order \uD83D\uDC49");
            createOrderButton.setStyle("-fx-font-size: 20px; -fx-background-color: FFEEDD;");
            createOrderButton.setOnAction(e -> {
                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppOrderStandard.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                    stage.setScene(scene);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            orderInfoVBox.getChildren().add(createOrderButton);
            orderInfoVBox.setAlignment(Pos.CENTER);  // Center the text and button
        }
    }

    private void FilltheScreen(Map<String, Map<Integer, Integer>> customer_current_order) {
        String order_status_str = CustomerUser.getCurrent_order_status();
        Text order_status = new Text(order_status_str);
        order_status.setStyle("-fx-font-size: 20px;");
        orderInfoVBox.getChildren().add(order_status);
        VBox.setMargin(order_status, new Insets(10, 0, 10, 0));

        if (order_status_str.equals("Order Submitted")) {
            order_status.setFill(Color.BLUE);
            order_status.setTextAlignment(TextAlignment.CENTER);
        } else if (order_status_str.equals("Order On Delivery")) {
            order_status.setFill(Color.GREEN);

            Text receiveOrderText = new Text("Did you receive your order?");
            receiveOrderText.setStyle("-fx-font-size: 15px;");
            orderInfoVBox.getChildren().add(receiveOrderText);
            orderInfoVBox.setAlignment(Pos.CENTER);  // Center the text

            Button yesButton = new Button("Yes");
            yesButton.setStyle("-fx-font-size: 15px; -fx-background-color: #bae1ff;");
            yesButton.setOnAction(e -> {
                CustomerUser.completeOrder();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("알림");
                alert.setHeaderText(null);
                alert.setContentText("배달 완료!");

                // Create the button and add a listener to close the alert
                ButtonType okButton = new ButtonType("확인");
                alert.getButtonTypes().setAll(okButton);

                alert.showAndWait();

                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPage.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                    stage.setScene(scene);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            Button noButton = new Button("No");
            noButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("알림");
                alert.setHeaderText(null);
                alert.setContentText("010-1111-1111 로 문의주세요.");

                // Create the button and add a listener to close the alert
                ButtonType okButton = new ButtonType("확인");
                alert.getButtonTypes().setAll(okButton);

                alert.showAndWait();

                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPage.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                    stage.setScene(scene);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            noButton.setStyle("-fx-font-size: 15px; -fx-background-color: #ffb3ba;");

            HBox buttonBox = new HBox(10, yesButton, noButton);
            buttonBox.setAlignment(Pos.CENTER);
            orderInfoVBox.getChildren().add(buttonBox);
        }

        double order_price = CustomerUser.getCurrent_order_price();
        Text order_price_text = new Text("Total Price: " + order_price);
        order_price_text.setStyle("-fx-font-size: 18px;");
        orderInfoVBox.getChildren().add(order_price_text);
        VBox.setMargin(order_price_text, new Insets(10, 0, 10, 0));

        Set<String> key_names = customer_current_order.keySet();
        for (String key_name : key_names) {
            Set<Integer> ids = customer_current_order.get(key_name).keySet();
            for (Integer id : ids) {

                Integer quantity = customer_current_order.get(key_name).get(id);
                System.out.println(key_name + "\t" + id + "\t" + quantity);
                get_set_names_from_db(key_name, id, quantity);
            }
        }
    }

    private void get_set_names_from_db(String key_name, Integer id, Integer quantity) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";
        String sql = "";

        if (key_name.equals("Pizza")) {
            sql = "SELECT name, pizza_size FROM pizza WHERE id = " + id;
        } else if (key_name.equals("Option")) {
            sql = "SELECT name FROM options WHERE id = " + id;
        }

        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);
            rs = DatabaseConnect.getSQLResult(conn, sql);
            if (rs.next()) {
                String name = rs.getString("name");
                String size = rs.getString("pizza_size");
                Text itemText = new Text(name + "\t Size: "+size+"\t Quantity: " + quantity);
                itemText.setStyle("-fx-font-size: 16px;");
                orderInfoVBox.getChildren().add(itemText);
            }
            DatabaseConnect.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

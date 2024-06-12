package yop.kosa_p1_yop;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicBoolean;


import java.util.*;

public class CustomerHistoryController extends CustomerMyPageController {

    @FXML
    private VBox orderInfoVBox; // FXML에서 정의한 VBox를 가져옵니다.

    // 여기서 DB에서 데이터를 가져와서 VBox에 추가하는 로직을 작성할 것입니다.
    public void initialize() {
        Map<Integer, List<String>> orderInfoList = CustomerUser.getHistory();
        // DB에서 데이터를 가져오는 로직을 구현하고, 아래 addOrderInfoToVBox 메서드를 호출하여 VBox에 데이터를 추가하세요.
        // 예를 들어, orderInfoList라는 ArrayList에 데이터를 담아두고,
        // 이 메서드에 orderInfoList를 전달하여 VBox에 데이터를 추가할 수 있습니다.
        addOrderInfoToVBox(orderInfoList);
    }

    // VBox에 Order 정보를 추가하는 메서드입니다.
    private void addOrderInfoToVBox(Map<Integer, List<String>> orderInfoMap) {
        if (orderInfoMap.isEmpty()) {
            Label noHistoryLabel = new Label("There's no Order History yet.\nPlease make an order !");
            noHistoryLabel.setStyle("-fx-font-size: 20px;");
            orderInfoVBox.getChildren().add(noHistoryLabel);
        } else {
            // orderInfoMap을 역순으로 순회하여 VBox에 추가합니다.
            List<Integer> orderIds = new ArrayList<>(orderInfoMap.keySet());
            Collections.reverse(orderIds); // order_id를 역순으로 배치

            for (Integer orderId : orderIds) {
                List<String> basicOrderInfos = orderInfoMap.get(orderId);
                StringBuilder basicInfo = new StringBuilder();
                for (String basicOrderInfo : basicOrderInfos) {
                    basicInfo.append(basicOrderInfo);
                }

                // 각 열에 대한 VBox를 생성합니다.
                VBox orderVBox = new VBox();
                orderVBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");

                // Order 정보를 보여주는 Label을 생성하여 VBox에 추가합니다.
                Label orderIdLabel = new Label("Order ID: " + orderId);
                Label orderInfoLabel = new Label(basicInfo.toString());
                orderVBox.getChildren().addAll(orderIdLabel, orderInfoLabel);

                // 상세정보 보기 버튼을 생성하여 이벤트 핸들러를 추가합니다.
                Button detailButton = new Button("Detail");

                // 플래그를 생성하여 추적합니다.
                AtomicBoolean detailVisible = new AtomicBoolean(false);

                detailButton.setOnAction(event -> {
                    // Toggle detail visibility
                    if (detailVisible.get()) {
                        // Remove detail VBox
                        orderVBox.getChildren().removeIf(node -> node instanceof VBox);
                        detailVisible.set(false);
                    } else {
                        // Add detail VBox
                        VBox detailVBox = createDetailVBox(orderId);
                        orderVBox.getChildren().add(detailVBox);
                        detailVisible.set(true);
                    }
                });

                orderVBox.getChildren().add(detailButton);

                // 생성된 열을 orderInfoVBox에 추가합니다.
                orderInfoVBox.getChildren().add(orderVBox);
            }
        }
    }

    private VBox createDetailVBox(int orderId) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";
        String sql = "";


        VBox detailVBox = new VBox();
        detailVBox.setStyle("-fx-padding: 10px;");


        sql = "select pizza_id, quantity from orders_pizza where orders_id = " + orderId;
        try {
            conn = DatabaseConnect.serverConnect(userid, passwd);
            rs = DatabaseConnect.getSQLResult(conn, sql);
            while (rs.next()) {
                Integer pizza_id = rs.getInt("pizza_id");
                Integer quantity = rs.getInt("quantity");
                String sql2 = "select name, pizza_size from pizza where id = " + pizza_id;
                try {
                    ResultSet rs2 = DatabaseConnect.getSQLResult(conn, sql2);
                    if (rs2.next()) {
                        String name = rs2.getString("name");
                        String size = rs2.getString("pizza_size");
                        Label pizzaDetailLabel = new Label(name + "\t Size: " + size + "\t Quantity: " + quantity);
                        detailVBox.getChildren().add(pizzaDetailLabel);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//
//        // Create a section for Options if they exist
//        if (!options.isEmpty()) {
//            Label optionsLabel = new Label("Options");
//            detailVBox.getChildren().add(optionsLabel);
//
//            for (Map<String, Integer> optionname_quantity : options.values()) {
//                for (Map.Entry<String, Integer> entry : optionname_quantity.entrySet()) {
//                    String name = entry.getKey();
//                    Integer quantity = entry.getValue();
//
//                    // Option name and quantity
//                    Label optionDetailLabel = new Label(name + ", Quantity: " + quantity);
//                    detailVBox.getChildren().add(optionDetailLabel);
//                }
//            }
//        }

        return detailVBox;
    }

}
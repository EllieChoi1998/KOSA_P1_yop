package yop.kosa_p1_yop;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;

public class CustomerHistoryController extends CustomerMainController {

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
                detailButton.setOnAction(event -> {
                    // Check if the detailed information has already been added to avoid duplicating it

                        // 상세 정보를 보여주는 칸을 생성하고, 해당 VBox에 추가합니다.
                        VBox detailVBox = new VBox();
                        detailVBox.setStyle("-fx-padding: 10px;");

                        // 상세 정보를 보여주는 내용을 추가합니다.
                        Map<String, Map<Integer, Map<String, Integer>>> detailedInfoList = CustomerUser.getDetailedHistory(orderId);
                        // get pizzas and options
                        Map<Integer, Map<String, Integer>> pizzas = detailedInfoList.get("Pizzas");
                        Map<Integer, Map<String, Integer>> options = detailedInfoList.get("Options");

                        // Create a section for Pizzas
                        if (!pizzas.isEmpty()) {
                            Label pizzasLabel = new Label("Pizzas");
                            detailVBox.getChildren().add(pizzasLabel);

                            Set<String> printedPizzas = new HashSet<>(); // To track printed pizzas

                            for (Map<String, Integer> pizzaname_quantity : pizzas.values()) {
                                for (Map.Entry<String, Integer> entry : pizzaname_quantity.entrySet()) {
                                    String name = entry.getKey();
                                    Integer quantity = entry.getValue();

                                    // Print the pizza only if it hasn't been printed already
                                    if (!printedPizzas.contains(name)) {
                                        // Pizza name and quantity
                                        Label pizzaDetailLabel = new Label(name + ", Quantity: " + quantity);
                                        detailVBox.getChildren().add(pizzaDetailLabel);
                                        printedPizzas.add(name); // Mark this pizza as printed
                                    }
                                }
                            }
                        }

                        // Create a section for Options if they exist
                        if (!options.isEmpty()) {
                            Label optionsLabel = new Label("Options");
                            detailVBox.getChildren().add(optionsLabel);

                            for (Map<String, Integer> optionname_quantity : options.values()) {
                                for (Map.Entry<String, Integer> entry : optionname_quantity.entrySet()) {
                                    String name = entry.getKey();
                                    Integer quantity = entry.getValue();

                                    // Option name and quantity
                                    Label optionDetailLabel = new Label(name + ", Quantity: " + quantity);
                                    detailVBox.getChildren().add(optionDetailLabel);
                                }
                            }
                        }

                        orderVBox.getChildren().add(detailVBox);

                });



                orderVBox.getChildren().add(detailButton);

                // 생성된 열을 orderInfoVBox에 추가합니다.
                orderInfoVBox.getChildren().add(orderVBox);
            }
        }
    }

    // Order 정보를 보여주는 커스텀 컨트롤을 생성하는 메서드입니다.
    private Pane createOrderInfoControl(String orderId, String orderInfo) {
        // Order 정보를 보여주는 컨트롤을 생성하고 반환합니다.
        // 예를 들어, Label을 이용하여 Order 정보를 보여줄 수 있습니다.
        Label orderIdLabel = new Label("Order ID: " + orderId);
        Label orderInfoLabel = new Label(orderInfo);
        VBox orderVBox = new VBox(orderIdLabel, orderInfoLabel);
        orderVBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");
        return orderVBox;
    }
}
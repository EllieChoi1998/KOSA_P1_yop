package yop.kosa_p1_yop;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class CustomerBucketController extends CustomerMyPageController{
    @FXML
    private VBox orderInfoVBox;

    public void initialize() throws IOException{
        Map<String, Map<Integer, Integer>> customer_bucket = CustomerUser.getBucket();
        if (!customer_bucket.isEmpty()) {
            FilltheScreen(customer_bucket);
        } else {
            Text noOrderText = new Text("장바구니가 비었습니다.\n장바구니에 원하는 음식을 추가해 주세요!");
            noOrderText.setStyle("-fx-font-size: 20px;");
            orderInfoVBox.getChildren().add(noOrderText);

            Button createOrderButton = new Button("Go to Create Order \uD83D\uDC49");
            createOrderButton.setStyle("-fx-font-size: 20px;  -fx-background-color: FEC88E;");

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

    private void FilltheScreen(Map<String, Map<Integer, Integer>> customer_bucket) throws IOException {
        Text order_status = new Text(" \uD83C\uDF55 현재 선택된 장바구니 음식들 \uD83C\uDF55");

        order_status.setStyle("-fx-font-size: 25px;");

        double bucket_price = CustomerUser.get_bucket_price();
        Text bucket_price_text = new Text("\uD83D\uDCB2 Total Price: " + bucket_price);
        bucket_price_text.setStyle("-fx-font-size: 18px;");
        orderInfoVBox.getChildren().add(bucket_price_text);
        VBox.setMargin(bucket_price_text, new Insets(10, 0, 10, 0));

        Set<String> key_names = customer_bucket.keySet();
        for (String key_name : key_names) {
            Set<Integer> ids = customer_bucket.get(key_name).keySet();
            for (Integer id : ids) {

                Integer quantity = customer_bucket.get(key_name).get(id);
                System.out.println(key_name + "\t" + id + "\t" + quantity);
                get_set_names_from_db(key_name, id, quantity);
            }
        }

        orderInfoVBox.getChildren().add(order_status);
        VBox.setMargin(order_status, new Insets(10, 0, 10, 0));


        Button checkout = new Button("결제하기");
        checkout.setStyle("-fx-font-size: 20px; -fx-background-color: #ffb3ba;");
        checkout.setOnAction(e -> {

            // 현재 주문이 있는지 확인
            if (CustomerUser.getCurrentOrder().size() == 0) {
                // 결제 메서드 선택 다이얼로그 표시
                ChoiceDialog<String> paymentMethodDialog = new ChoiceDialog<>("Card", "Card", "Rewards");
                paymentMethodDialog.setTitle("결제 방법");
                paymentMethodDialog.setHeaderText("결제 방법 선택");
                paymentMethodDialog.setContentText("결제 방법을 선택하세요:");

                Optional<String> result = paymentMethodDialog.showAndWait();

                if (result.isPresent()) {
                    String selectedMethod = result.get();
                    AtomicBoolean checkout_success = new AtomicBoolean(false);

                    if (selectedMethod.equals("Card")) {
                        // 카드 결제 다이얼로그 표시
                        Dialog<String[]> cardPaymentDialog = new Dialog<>();
                        cardPaymentDialog.setTitle("카드 결제");
                        cardPaymentDialog.setHeaderText("카드 정보 입력");

                        // 버튼 유형 설정
                        ButtonType confirmButtonType = new ButtonType("확인", ButtonBar.ButtonData.OK_DONE);
                        cardPaymentDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                        // 카드 번호, CVV, 비밀번호 입력 필드 생성
                        TextField cardNumberField = new TextField();
                        cardNumberField.setPromptText("카드 번호");
                        PasswordField cvvField = new PasswordField();
                        cvvField.setPromptText("CVV");
                        PasswordField passwordField = new PasswordField();
                        passwordField.setPromptText("비밀번호");

                        // 입력 필드를 다이얼로그에 추가
                        cardPaymentDialog.getDialogPane().setContent(new VBox(10, cardNumberField, cvvField, passwordField));

                        // 카드 번호 필드에 초점 설정
                        Platform.runLater(cardNumberField::requestFocus);

                        // 확인 버튼 클릭 시 결과를 문자열 배열로 변환
                        cardPaymentDialog.setResultConverter(dialogButton -> {
                            if (dialogButton == confirmButtonType) {
                                return new String[]{cardNumberField.getText(), cvvField.getText(), passwordField.getText()};
                            }
                            return null;
                        });

                        Optional<String[]> cardDetails = cardPaymentDialog.showAndWait();

                        cardDetails.ifPresent(details -> {
                            if (details.length == 3) {
                                try {
                                    checkout_success.set(CustomerUser.payToCheckout_card(details[0], details[1], details[2]));
                                } catch (Exception ex) {
                                    System.out.println("오류 발생");
                                    throw new RuntimeException(ex);
                                }
                            }
                        });
                    } else if (selectedMethod.equals("Rewards")) {
                        // 리워드 결제 다이얼로그 표시
                        TextInputDialog rewardsPaymentDialog = new TextInputDialog();
                        rewardsPaymentDialog.setTitle("리워드 결제");
                        rewardsPaymentDialog.setHeaderText("리워드 포인트 사용");
                        rewardsPaymentDialog.setContentText("현재 포인트: " + CustomerUser.getCredits() + "\n리워드 포인트를 사용하시겠습니까? (예/아니오)");

                        Optional<String> useRewards = rewardsPaymentDialog.showAndWait();

                        if (useRewards.isPresent() && useRewards.get().equalsIgnoreCase("yes")) {
                            checkout_success.set(CustomerUser.payToCheckout_rewards());
                        }
                    }

                    if (checkout_success.get()) {
                        // 주문 확인 화면으로 이동
                        Stage stage = (Stage) AppMain.getPrimaryStage();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Customer_CurrentOrder.fxml"));
                        try {
                            Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                            stage.setScene(scene);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("주의");
                alert.setHeaderText("진행 중인 주문이 이미 존재합니다.");
                alert.showAndWait();
            }
        });



        Button orderMore = new Button("메뉴 선택으로 돌아가기");
        orderMore.setStyle("-fx-font-size: 20px; -fx-background-color: #bae1ff;");
        orderMore.setOnAction(e -> {
            Stage stage = (Stage) AppMain.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppOrderStandard.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                stage.setScene(scene);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        HBox buttonBox = new HBox(10, checkout, orderMore);
        buttonBox.setAlignment(Pos.CENTER);
        orderInfoVBox.getChildren().add(buttonBox);

    }

    private void get_set_names_from_db(String key_name, Integer id, Integer quantity) {
        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";
        String sql = "";

        if (key_name.equals("Pizza")) {
            sql = "SELECT name, pizza_size, price FROM pizza WHERE id = " + id;
            try {
                conn = DatabaseConnect.serverConnect(userid, passwd);
                rs = DatabaseConnect.getSQLResult(conn, sql);
                if (rs.next()) {
                    String name = rs.getString("name");
                    String size = rs.getString("pizza_size");
                    Double price = rs.getDouble("price");
                    Text itemText = new Text(name + " "+ size + ":\t" + quantity + "개\t총 가격: "+ quantity*price);
                    itemText.setStyle("-fx-font-size: 16px;");
                    orderInfoVBox.getChildren().add(itemText);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (key_name.equals("Option")) {
            sql = "SELECT name, price FROM options WHERE id = " + id;
            try {
                conn = DatabaseConnect.serverConnect(userid, passwd);
                rs = DatabaseConnect.getSQLResult(conn, sql);
                if (rs.next()) {
                    String name = rs.getString("name");
                    Double price = rs.getDouble("price");
                    Text itemText = new Text(name + " " + quantity + "개\t총 가격: "+ quantity*price);
                    itemText.setStyle("-fx-font-size: 18px;");
                    orderInfoVBox.getChildren().add(itemText);
                }
                DatabaseConnect.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DatabaseConnect.closeConnection(conn);
            }
        }


    }
}

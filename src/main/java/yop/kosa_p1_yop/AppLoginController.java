package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import java.sql.*;

public class AppLoginController {
    @FXML
    private TextField idinput;

    @FXML
    private PasswordField passwordinput;

    @FXML
    private void handleLoginPageButtonAction() throws IOException {
        String id = idinput.getText();
        String password = passwordinput.getText();
        boolean customer_authenticated = is_customer(id, password);
        if (customer_authenticated) {
            Stage stage = (Stage) AppMain.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMyPage.fxml"));
            Parent root = fxmlLoader.load();
            CustomerMyPageController controller = fxmlLoader.getController();
            controller.setTextElements(CustomerUser.getName(), CustomerUser.getId(), CustomerUser.getCredits() );
            Scene scene = new Scene(root, 450, 820);
            stage.setScene(scene);
        } else {
            boolean admin_authenticated = is_admin(id, password);
            if (admin_authenticated) {
                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(
                        getClass().getResource("AppAdminPage.fxml"));
                Parent root = fxmlLoader.load();
                AdminMainController controller = fxmlLoader.getController();
                controller.setTextElements(AdminUser.getName(), AdminUser.getId(), AdminUser.getmyrole());
                Scene scene = new Scene(root, 450, 820);
                stage.setScene(scene);
            }
            else {
                // Show alert for invalid user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("로그인 실패");
                alert.setHeaderText(null);
                alert.setContentText("해당 아이디의 유저를 찾을 수 없습니다.\n다시 시도해 주세요.");
                alert.showAndWait();
            }
        }

    }

    private boolean is_customer(String id, String pwd) {

        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT name, credits FROM customer WHERE id = '" + id + "' AND pwd = '" + pwd + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String name = rs.getString("name");
                long credits = rs.getLong("credits");
                CustomerUser.initialize(id,pwd,name,credits);
                return true;
                }



            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }
        return false;
    }

    private boolean is_admin(String id, String pwd) {

        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT name FROM admin WHERE id = '" + id + "' AND pwd = '" + pwd + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String name = rs.getString("name");
                AdminUser.initialize(id,pwd,name);
                return true;
            }

            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }
        return false;
    }

}

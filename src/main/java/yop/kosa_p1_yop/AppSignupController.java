package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

public class AppSignupController {
    @FXML
    private TextField idinput;

    @FXML
    private PasswordField passwordinput;

    @FXML
    private TextField usernameinput;

    @FXML
    private void handleSignupButtonAction() throws IOException {
        Connection con = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try{
            con = DatabaseConnect.serverConnect(userid, passwd);

            String id = idinput.getText();
            String pwd = passwordinput.getText();
            String name = usernameinput.getText();

            if(!is_customer(id, pwd) && !is_admin(id, pwd)){
                String sql = "INSERT INTO customer (id, pwd, name) VALUES ('"+id+"', '"+pwd+"', '"+name+"')";
                DatabaseConnect.getSQLResult(con, sql);
                DatabaseConnect.commit(con);
                DatabaseConnect.closeConnection(con);

                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                stage.setScene(scene);

            } else{

                    // Show alert for invalid user
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("회원가입 실패");
                    alert.setHeaderText(null);
                    alert.setContentText("해당 아이디의 유저가 이미 존재합니다.\n다시 시도해 주세요.");
                    alert.showAndWait();
                }
                DatabaseConnect.closeConnection(con);

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private boolean is_customer(String id, String pwd) {

        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT name, credits FROM customer WHERE id = '" + id + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String name = rs.getString("name");
                if(name != null)
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

            String sql = "SELECT name FROM admin WHERE id = '" + id + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String name = rs.getString("name");
                if(name != null)
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

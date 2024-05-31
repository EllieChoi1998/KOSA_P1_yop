package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        //System.out.println("=====\n"+id+"\t"+password+"\n======");
        boolean authenticated = is_user(id, password);
        if (authenticated) {
            Stage stage = (Stage) AppMain.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("CustomerMain.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 820);
            stage.setScene(scene);
        }
        else System.out.println("Invalid user");
    }

    private boolean is_user(String id, String pwd) {

        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT name, role FROM users WHERE id = '" + id + "' AND pwd = '" + pwd + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String role = rs.getString("role");
                String name = rs.getString("name");

                if(role.equals("C"))
                {
                    CustomerUser user = new CustomerUser(userid, name);
                    System.out.println("===========\n"+name+"\n========");
                    return true;
                }

//                if (role == "C") {
//                    String name = rs.getString("name");
//                    CustomerUser user = new CustomerUser(userid, name);
//                    System.out.println("===========\n"+name+"\n========");
//                    return true;
//                }
            }

            DatabaseConnect.closeResources(rs, conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                System.out.println("EROOR");
                e.printStackTrace();  // Print stack trace to console
            }
        }
        return false;
    }

}

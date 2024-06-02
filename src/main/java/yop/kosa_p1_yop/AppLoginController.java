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
        int authenticated = is_user(id, password);
        if (authenticated == 0) {
            Stage stage = (Stage) AppMain.getPrimaryStage();
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("CustomerMain.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 820);
            stage.setScene(scene);
        }
        else System.out.println("Invalid user");
    }

    private int is_user(String id, String pwd) {

        Connection conn = null;
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {

            conn = DatabaseConnect.serverConnect(userid, passwd);

            String sql = "SELECT name, role, credits FROM users WHERE id = '" + id + "' AND pwd = '" + pwd + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String role = rs.getString("role");
                String name = rs.getString("name");
                long credits = rs.getLong("credits");

                if(role.equals("C"))
                {
                    CustomerUser.initialize(id,pwd,name,credits);
                    return 0;
                }
                else if(role.equals("A")){
                    return 1;
                }
            }

            DatabaseConnect.closeResultSet(rs);
            DatabaseConnect.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }
        return 2;
    }

}

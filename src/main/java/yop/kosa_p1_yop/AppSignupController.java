package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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

            if(!is_user(id, pwd, con)){
                String sql = "INSERT INTO users (id, pwd, name) VALUES ('"+id+"', '"+pwd+"', '"+name+"')";
                DatabaseConnect.getSQLResult(con, sql);
                DatabaseConnect.commit(con);
                DatabaseConnect.closeConnection(con);

                Stage stage = (Stage) AppMain.getPrimaryStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AppMain.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 450, 820);
                stage.setScene(scene);

            } else{
                System.out.println("Existing user. Please try another id");
                DatabaseConnect.closeConnection(con);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private boolean is_user(String id, String pwd, Connection conn){
        ResultSet rs = null;
        String userid = "pizza_admin";
        String passwd = "admin";

        try {
            String sql = "SELECT name FROM users WHERE id = '" + id + "' AND pwd = '" + pwd + "'";

            rs = DatabaseConnect.getSQLResult(conn, sql);

            if (rs.next()) {
                String name = rs.getString("name");

                System.out.println("=========\n"+name+"\n==========");
                if(name != null)
                {
                    return true;
                }
            }

            DatabaseConnect.closeResultSet(rs);

        } catch (Exception e) {
            e.printStackTrace();  // Print stack trace to console
        }

        return false;
    }


}

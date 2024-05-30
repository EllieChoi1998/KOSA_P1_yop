package yop.kosa_p1_yop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.sql.*;

public class AppLoginController {

    @FXML
    private void handleLoginPageButtonAction() throws IOException {

        Stage stage = (Stage) AppMain.getPrimaryStage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 820);
        stage.setScene(scene);
    }

    private boolean is_user(String id, String pwd) {
        DatabaseConnect dc = new DatabaseConnect();
        Connection conn = null;
        ResultSet rs = null;
        String userid = "scott";
        String passwd = "tiger";

        try {

            conn = dc.serverConnect(userid, passwd);

            String sql = "SELECT COUNT(*) FROM users WHERE id = '" + id + "' AND pwd = '" + pwd + "'";


            rs = dc.getSQLResult(conn, sql);

            if (rs.next()) {
                int count = rs.getInt(1);  // Get the value of the first column (which is the count)
                if (count == 1) return true;
            }

            dc.closeResources(rs, conn);

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

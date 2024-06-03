package yop.kosa_p1_yop;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class AddToppingsController extends CustomPizzaController{

    @FXML
    private ImageView pepperonitopping;
    @FXML
    private void handlePepperoniAddAction() {
        // Make the additional ImageView visible
        pepperonitopping.setVisible(true);
    }

    @FXML
    private void handlePepperoniDeleteAction() {
        // Make the additional ImageView visible
        pepperonitopping.setVisible(false);
    }

    @FXML
    private ImageView pineappletopping;
    @FXML
    private void handlePinaeappleAddAction() {
        // Make the additional ImageView visible
        pineappletopping.setVisible(true);
    }
    @FXML
    private void handlePineappleDeleteAction() {
        // Make the additional ImageView visible
        pineappletopping.setVisible(false);
    }

    @FXML
    private ImageView bacontopping;
    @FXML
    private void handleBaconAddAction() {
        // Make the additional ImageView visible
        bacontopping.setVisible(true);
    }
    @FXML
    private void handleBaconDeleteAction() {
        // Make the additional ImageView visible
        bacontopping.setVisible(false);
    }

    @FXML
    private ImageView bulgogitopping;
    @FXML
    private void handleBulgogiAddAction() {
        // Make the additional ImageView visible
        bulgogitopping.setVisible(true);
    }
    @FXML
    private void handleBulgogiDeleteAction() {
        // Make the additional ImageView visible
        bulgogitopping.setVisible(false);
    }

    @FXML
    private ImageView shrimptopping;
    @FXML
    private void handleShrimpAddAction() {
        // Make the additional ImageView visible
        shrimptopping.setVisible(true);
    }
    @FXML
    private void handleShrimpDeleteAction() {
        // Make the additional ImageView visible
        shrimptopping.setVisible(false);
    }

    @FXML
    private ImageView oniontopping;
    @FXML
    private void handleOnionAddAction() {
        // Make the additional ImageView visible
        oniontopping.setVisible(true);
    }
    @FXML
    private void handleOnionDeleteAction() {
        // Make the additional ImageView visible
        oniontopping.setVisible(false);
    }

    @FXML
    private ImageView capsicumtopping;
    @FXML
    private void handleCapsicumAddAction() {
        // Make the additional ImageView visible
        capsicumtopping.setVisible(true);
    }
    @FXML
    private void handleCapsicumDeleteAction() {
        // Make the additional ImageView visible
        capsicumtopping.setVisible(false);
    }
    @FXML
    private ImageView hamtopping;
    @FXML
    private void handleHamAddAction() {
        // Make the additional ImageView visible
        hamtopping.setVisible(true);
    }
    @FXML
    private void handleHamDeleteAction() {
        // Make the additional ImageView visible
        hamtopping.setVisible(false);
    }
}

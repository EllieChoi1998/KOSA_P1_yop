package yop.kosa_p1_yop;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class AddToppingsController extends CustomPizzaController{
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("두 번 이상 클릭할 수 없습니다.");
        alert.showAndWait();
    }
    private void pepperoniresetActions() {
        pepperoniaddButtonClicked = false;
        pepperonideleteButtonClicked = false;
        pepperonicurrentAction = "";
    }
    @FXML
    private ImageView pepperonitopping;
    private boolean pepperoniaddButtonClicked = false;
    private boolean pepperonideleteButtonClicked = false;
    private String pepperonicurrentAction = "";

    @FXML
    private void handlePepperoniAddAction() {
        if (!pepperoniaddButtonClicked && !"add".equals(pepperonicurrentAction)) {
            // 첫 번째 클릭 처리
            pepperoniaddButtonClicked = true;
            pepperonicurrentAction = "add";
            pepperonitopping.setDisable(false);
            pepperonitopping.setVisible(true);
        } else if (pepperoniaddButtonClicked && "add".equals(pepperonicurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            pepperoniresetActions();
            handlePepperoniAddAction();
        }
    }

    @FXML
    private void handlePepperoniDeleteAction() {
        // Make the additional ImageView visible
        if (!pepperonideleteButtonClicked && !"delete".equals(pepperonicurrentAction)) {
            // 첫 번째 클릭 처리
            pepperonideleteButtonClicked = true;
            pepperonicurrentAction = "delete";
            pepperonitopping.setDisable(false);
            pepperonitopping.setVisible(false);
        } else if (pepperonideleteButtonClicked && "delete".equals(pepperonicurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            pepperoniresetActions();
            handlePepperoniDeleteAction();
        }

    }

    private void pineappleresetActions() {
        pineappleaddButtonClicked = false;
        pineappledeleteButtonClicked = false;
        pineapplecurrentAction = "";
    }
    @FXML
    private ImageView pineappletopping;
    private boolean pineappleaddButtonClicked = false;
    private boolean pineappledeleteButtonClicked = false;
    private String pineapplecurrentAction = "";

    @FXML
    private void handlePinaeappleAddAction() {
        if (!pineappleaddButtonClicked && !"add".equals(pineapplecurrentAction)) {
            // 첫 번째 클릭 처리
            pineappleaddButtonClicked = true;
            pineapplecurrentAction = "add";
            pineappletopping.setDisable(false);
            pineappletopping.setVisible(true);
        } else if (pineappleaddButtonClicked && "add".equals(pineapplecurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            pineappleresetActions();
            handlePinaeappleAddAction();
        }
    }

    @FXML
    private void handlePineappleDeleteAction() {
        // Make the additional ImageView visible
        if (!pineappledeleteButtonClicked && !"delete".equals(pineapplecurrentAction)) {
            // 첫 번째 클릭 처리
            pineappledeleteButtonClicked = true;
            pineapplecurrentAction = "delete";
            pineappletopping.setDisable(false);
            pineappletopping.setVisible(false);
        } else if (pineappledeleteButtonClicked && "delete".equals(pineapplecurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            pineappleresetActions();
            handlePineappleDeleteAction();
        }

    }

    private void baconresetActions() {
        baconaddButtonClicked = false;
        bacondeleteButtonClicked = false;
        baconcurrentAction = "";
    }
    @FXML
    private ImageView bacontopping;
    private boolean baconaddButtonClicked = false;
    private boolean bacondeleteButtonClicked = false;
    private String baconcurrentAction = "";

    @FXML
    private void handleBaconAddAction() {
        if (!baconaddButtonClicked && !"add".equals(baconcurrentAction)) {
            // 첫 번째 클릭 처리
            baconaddButtonClicked = true;
            baconcurrentAction = "add";
            bacontopping.setDisable(false);
            bacontopping.setVisible(true);
        } else if (baconaddButtonClicked && "add".equals(baconcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            baconresetActions();
            handleBaconAddAction();
        }
    }
    @FXML
    private void handleBaconDeleteAction() {
        // Make the additional ImageView visible
        if (!bacondeleteButtonClicked && !"delete".equals(baconcurrentAction)) {
            // 첫 번째 클릭 처리
            bacondeleteButtonClicked = true;
            baconcurrentAction = "delete";
            bacontopping.setDisable(false);
            bacontopping.setVisible(false);
        } else if (bacondeleteButtonClicked && "delete".equals(baconcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            baconresetActions();
            handleBaconDeleteAction();
        }

    }

    private void bulgogiresetActions() {
        bulgogiaddButtonClicked = false;
        bulgogideleteButtonClicked = false;
        bulgogicurrentAction = "";
    }
    @FXML
    private ImageView bulgogitopping;
    private boolean bulgogiaddButtonClicked = false;
    private boolean bulgogideleteButtonClicked = false;
    private String bulgogicurrentAction = "";

    @FXML
    private void handleBulgogiAddAction() {
        if (!bulgogiaddButtonClicked && !"add".equals(bulgogicurrentAction)) {
            // 첫 번째 클릭 처리
            bulgogiaddButtonClicked = true;
            bulgogicurrentAction = "add";
            bulgogitopping.setDisable(false);
            bulgogitopping.setVisible(true);
        } else if (bulgogiaddButtonClicked && "add".equals(bulgogicurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            bulgogiresetActions();
            handleBulgogiAddAction();
        }
    }
    @FXML
    private void handleBulgogiDeleteAction() {
        // Make the additional ImageView visible
        if (!bulgogideleteButtonClicked && !"delete".equals(bulgogicurrentAction)) {
            // 첫 번째 클릭 처리
            bulgogideleteButtonClicked = true;
            bulgogicurrentAction = "delete";
            bulgogitopping.setDisable(false);
            bulgogitopping.setVisible(false);
        } else if (bulgogideleteButtonClicked && "delete".equals(bulgogicurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            bulgogiresetActions();
            handleBulgogiDeleteAction();
        }
    }

    private void shrimpresetActions() {
        shrimpaddButtonClicked = false;
        shrimpdeleteButtonClicked = false;
        shrimpcurrentAction = "";
    }
    @FXML
    private ImageView shrimptopping;
    private boolean shrimpaddButtonClicked = false;
    private boolean shrimpdeleteButtonClicked = false;
    private String shrimpcurrentAction = "";

    @FXML
    private void handleShrimpAddAction() {
        if (!shrimpaddButtonClicked && !"add".equals(shrimpcurrentAction)) {
            // 첫 번째 클릭 처리
            shrimpaddButtonClicked = true;
            shrimpcurrentAction = "add";
            shrimptopping.setDisable(false);
            shrimptopping.setVisible(true);
        } else if (shrimpaddButtonClicked && "add".equals(shrimpcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            shrimpresetActions();
            handleShrimpAddAction();
        }
    }

    @FXML
    private void handleShrimpDeleteAction() {
        // Make the additional ImageView visible
        if (!shrimpdeleteButtonClicked && !"delete".equals(shrimpcurrentAction)) {
            // 첫 번째 클릭 처리
            shrimpdeleteButtonClicked = true;
            shrimpcurrentAction = "delete";
            shrimptopping.setDisable(false);
            shrimptopping.setVisible(false);
        } else if (shrimpdeleteButtonClicked && "delete".equals(shrimpcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            shrimpresetActions();
            handleShrimpDeleteAction();
        }
    }

    private void onionresetActions() {
        onionaddButtonClicked = false;
        oniondeleteButtonClicked = false;
        onioncurrentAction = "";
    }
    @FXML
    private ImageView oniontopping;
    private boolean onionaddButtonClicked = false;
    private boolean oniondeleteButtonClicked = false;
    private String onioncurrentAction = "";

    @FXML
    private void handleOnionAddAction() {
        if (!onionaddButtonClicked && !"add".equals(onioncurrentAction)) {
            // 첫 번째 클릭 처리
            onionaddButtonClicked = true;
            onioncurrentAction = "add";
            oniontopping.setDisable(false);
            oniontopping.setVisible(true);
        } else if (onionaddButtonClicked && "add".equals(onioncurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            onionresetActions();
            handleOnionAddAction();
        }
    }
    @FXML
    private void handleOnionDeleteAction() {
        // Make the additional ImageView visible
        if (!oniondeleteButtonClicked && !"delete".equals(onioncurrentAction)) {
            // 첫 번째 클릭 처리
            oniondeleteButtonClicked = true;
            onioncurrentAction = "delete";
            oniontopping.setDisable(false);
            oniontopping.setVisible(false);
        } else if (oniondeleteButtonClicked && "delete".equals(onioncurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            onionresetActions();
            handleOnionDeleteAction();
        }
    }

    private void capsicumresetActions() {
        capsicumaddButtonClicked = false;
        capsicumdeleteButtonClicked = false;
        capsicumcurrentAction = "";
    }
    @FXML
    private ImageView capsicumtopping;
    private boolean capsicumaddButtonClicked = false;
    private boolean capsicumdeleteButtonClicked = false;
    private String capsicumcurrentAction = "";

    @FXML
    private void handleCapsicumAddAction() {
        if (!capsicumaddButtonClicked && !"add".equals(capsicumcurrentAction)) {
            // 첫 번째 클릭 처리
            capsicumaddButtonClicked = true;
            capsicumcurrentAction = "add";
            capsicumtopping.setDisable(false);
            capsicumtopping.setVisible(true);
        } else if (capsicumaddButtonClicked && "add".equals(capsicumcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            capsicumresetActions();
            handleCapsicumAddAction();
        }
    }
    @FXML
    private void handleCapsicumDeleteAction() {
        // Make the additional ImageView visible
        if (!capsicumdeleteButtonClicked && !"delete".equals(capsicumcurrentAction)) {
            // 첫 번째 클릭 처리
            capsicumdeleteButtonClicked = true;
            capsicumcurrentAction = "delete";
            capsicumtopping.setDisable(false);
            capsicumtopping.setVisible(false);
        } else if (capsicumdeleteButtonClicked && "delete".equals(capsicumcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            capsicumresetActions();
            handleCapsicumDeleteAction();
        }
    }

    private void hamresetActions() {
        hamaddButtonClicked = false;
        hamdeleteButtonClicked = false;
        hamcurrentAction = "";
    }
    @FXML
    private ImageView hamtopping;
    private boolean hamaddButtonClicked = false;
    private boolean hamdeleteButtonClicked = false;
    private String hamcurrentAction = "";

    @FXML
    private void handleHamAddAction() {
        if (!hamaddButtonClicked && !"add".equals(hamcurrentAction)) {
            // 첫 번째 클릭 처리
            hamaddButtonClicked = true;
            hamcurrentAction = "add";
            hamtopping.setDisable(false);
            hamtopping.setVisible(true);
        } else if (hamaddButtonClicked && "add".equals(hamcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            hamresetActions();
            handleHamAddAction();
        }
    }
    @FXML
    private void handleHamDeleteAction() {
        // Make the additional ImageView visible
        if (!hamdeleteButtonClicked && !"delete".equals(hamcurrentAction)) {
            // 첫 번째 클릭 처리
            hamdeleteButtonClicked = true;
            hamcurrentAction = "delete";
            hamtopping.setDisable(false);
            hamtopping.setVisible(false);
        } else if (hamdeleteButtonClicked && "delete".equals(hamcurrentAction)) {
            // 두 번째 클릭 시 경고 메시지 표시
            showAlert();
        } else {
            hamresetActions();
            handleHamDeleteAction();
        }
    }
}

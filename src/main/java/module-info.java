module yop.kosa_p1_yop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens yop.kosa_p1_yop to javafx.fxml;
    exports yop.kosa_p1_yop;
}
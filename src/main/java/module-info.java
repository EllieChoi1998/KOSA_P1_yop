module yop.kosa_p1_yop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires spring.core;


    opens yop.kosa_p1_yop to javafx.fxml;
    exports yop.kosa_p1_yop;
}
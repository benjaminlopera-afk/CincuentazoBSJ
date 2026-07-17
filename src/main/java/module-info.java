module com.example.cincuentazo_bsj {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.cincuentazo_bsj to javafx.fxml;
    opens com.example.cincuentazo_bsj.applications to javafx.fxml;
    opens com.example.cincuentazo_bsj.controllers to javafx.fxml;

    exports com.example.cincuentazo_bsj;
    exports com.example.cincuentazo_bsj.applications;
}
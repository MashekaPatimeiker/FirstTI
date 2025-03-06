module com.example.ti_mark2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ti_mark2 to javafx.fxml;
    exports com.example.ti_mark2;
}
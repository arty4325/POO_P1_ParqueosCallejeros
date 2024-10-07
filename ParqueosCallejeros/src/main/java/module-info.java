module com.example.parqueoscallejeros {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.example.parqueoscallejeros to javafx.fxml;
    exports com.example.parqueoscallejeros;
}
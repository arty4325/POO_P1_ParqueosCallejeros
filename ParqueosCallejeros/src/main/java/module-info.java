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
    opens com.example.parqueoscallejeros.User.UserLogin to javafx.fxml;  // Abre el paquete para FXML

    exports com.example.parqueoscallejeros;
    exports com.example.parqueoscallejeros.User.UserLogin;
}

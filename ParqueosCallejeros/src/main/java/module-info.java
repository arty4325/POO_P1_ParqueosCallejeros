module com.example.parqueoscallejeros {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    requires java.mail; // Necesaria para enviar correos
    requires jakarta.activation;
    requires jdk.localedata; // Añade esta línea si usas Jakarta Activation

    opens com.example.parqueoscallejeros to javafx.fxml;
    opens com.example.parqueoscallejeros.User.UserLogin to javafx.fxml;  // Abre el paquete para FXML
    opens com.example.parqueoscallejeros.User.UserMainFunctions to javafx.fxml;


    exports com.example.parqueoscallejeros;
    exports com.example.parqueoscallejeros.User.UserLogin;
    exports com.example.parqueoscallejeros.User.UserMain;
}

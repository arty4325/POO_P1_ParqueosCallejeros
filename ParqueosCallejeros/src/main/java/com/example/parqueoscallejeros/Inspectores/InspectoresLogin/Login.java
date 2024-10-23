package com.example.parqueoscallejeros.Inspectores.InspectoresLogin;

import com.example.parqueoscallejeros.Inspectores.InspectoresMain.MainController;
import com.example.parqueoscallejeros.Reportes.Session;
import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sound.midi.MidiChannel;
import java.io.IOException;

public class Login {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField idUsuario;

    @FXML
    private PasswordField idContra;

    @FXML
    private Label infoInic;

    /**
     * Funcion que permite iniciar sesion a un inspector
     * @param event
     * @throws IOException
     */
    public void inicSesion(ActionEvent event) throws IOException {
        String contraString = idContra.getText(); // id contra es un espacio de entrada

        // Validación de que solo contiene números y es de 4 dígitos
        if (contraString.matches("\\d{4}")) {
            // Si es válido, continua
            infoInic.setText("Pin valido");
            DatabaseManager databaseManager = new DatabaseManager();
            if(databaseManager.iniciarSesionInspector(idUsuario.getText(), idContra.getText())) {
                System.out.println("Se ha iniciado la sesion");

                int id = databaseManager.obtenerIdInspector(idUsuario.getText(), idContra.getText());
                Session.getInstance().setUserId(id);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Inspectores/InspectoresMain/MainInspectores.fxml"));
                Parent root = loader.load();

                // Obtén el controlador del nuevo FXML
                MainController controller = loader.getController();

                // Pasa el valor (por ejemplo, el idUsuario)
                controller.setUserData(id, idUsuario.getText(), idContra.getText());

                // Cambia la escena
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else {
                infoInic.setText("La contraseña no es correcta");
            }


        } else {
            // mensaje a la ui
            infoInic.setText("Note que el pin tiene que ser de 4 digitos numericos");
        }
    }

}

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.App.setRoot;
import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;
//
public class firstController {

    @FXML private TextField IPtext;
    @FXML private TextField PortText;
    @FXML private Button StartButton;
    @FXML private AnchorPane root;

    @FXML
    void StartTheGame(ActionEvent event) {
        try {
            client = SimpleClient.getClient(IPtext.getText(), Integer.parseInt(PortText.getText()));
            client.openConnection();
            client.sendToServer("add client");

            Platform.runLater(() -> {
                try {
                    setRoot("primary");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load primary.fxml", e);
                }
            });

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Failed to start game: " + e.getMessage(), e);
        }
    }
}

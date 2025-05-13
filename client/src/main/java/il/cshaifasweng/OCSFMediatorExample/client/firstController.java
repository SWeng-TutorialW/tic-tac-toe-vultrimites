package il.cshaifasweng.OCSFMediatorExample.client;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;

import static il.cshaifasweng.OCSFMediatorExample.client.App.setRoot;

public class firstController {
    @FXML private TextField IPtext;
    @FXML private TextField PortText;

    @FXML
    void StartTheGame(ActionEvent event) {
        String host = IPtext.getText().trim();
        int port = Integer.parseInt(PortText.getText().trim());
        SimpleClient client = SimpleClient.getClient(host, port);
        try {
            client.openConnection();
            client.sendToServer("add client");
            setRoot("primary");
        } catch (IOException e) {
            System.err.println("Cannot start game: " + e.getMessage());
        }
    }
}


//public class firstController {
//
//    @FXML private TextField IPtext;
//    @FXML private TextField PortText;
//    @FXML private Button StartButton;
//    @FXML private AnchorPane root;
//
//    @FXML
//    void StartTheGame(ActionEvent event) {
//        try {
//            client = SimpleClient.getClient(IPtext.getText(), Integer.parseInt(PortText.getText()));
//            client.openConnection();
//            client.sendToServer("add client");
//
//            Platform.runLater(() -> {
//                try {
//                    setRoot("primary");
//                } catch (IOException e) {
//                    throw new RuntimeException("Failed to load primary.fxml", e);
//                }
//            });
//
//        } catch (IOException | NumberFormatException e) {
//            throw new RuntimeException("Failed to start game: " + e.getMessage(), e);
//        }
//    }
//}
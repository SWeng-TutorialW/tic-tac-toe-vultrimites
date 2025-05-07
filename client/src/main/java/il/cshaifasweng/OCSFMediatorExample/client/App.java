package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        // Start with first.fxml (IP + Port entry screen)
        scene = new Scene(loadFXML("first"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe - Connect");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        EventBus.getDefault().unregister(this);
        if (SimpleClient.client != null && SimpleClient.client.isConnected()) {
            SimpleClient.client.sendToServer("remove client");
            SimpleClient.client.closeConnection();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}

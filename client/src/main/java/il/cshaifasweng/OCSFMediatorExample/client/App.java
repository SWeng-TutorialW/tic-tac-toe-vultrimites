package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("first"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe - Connect");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return loader.load();
    }

    @Override
    public void stop() throws Exception {
        // unregister from EventBus
        EventBus.getDefault().unregister(this);

        // grab the single client instance
        SimpleClient client = SimpleClient.getClient();
        if (client != null && client.isConnected()) {
            client.sendToServer("remove client");
            client.closeConnection();
        }

        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
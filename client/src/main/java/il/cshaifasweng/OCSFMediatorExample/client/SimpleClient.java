package il.cshaifasweng.OCSFMediatorExample.client;

import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.entities.gameMove;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	
	public static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof gameMove) {
			EventBus.getDefault().post(msg);
		} else if (msg instanceof String) {
			String str = (String) msg;
			if (str.equals("start game")) {
				// Switch to the primary game view when both players have connected
				javafx.application.Platform.runLater(() -> {
					try {
						il.cshaifasweng.OCSFMediatorExample.client.App.setRoot("primary");
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				System.out.println("Received from server: " + str);
			}
		}
	}

	public static SimpleClient getClient(String host, int port){
		if (client == null){
			client = new SimpleClient(host, port);
		}
		return client;
	}

	public void sendMove(gameMove move){
		try{
			this.sendToServer(move);
		} catch (Exception e) {
			System.err.println("Failed to send move: " + e.getMessage());
		}
	}

}

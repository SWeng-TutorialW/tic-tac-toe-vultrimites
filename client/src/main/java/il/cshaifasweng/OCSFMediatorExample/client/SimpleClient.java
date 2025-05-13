package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import java.io.IOException;
import org.greenrobot.eventbus.EventBus;
import il.cshaifasweng.OCSFMediatorExample.entities.gameMove;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

public class SimpleClient extends AbstractClient {
	private static SimpleClient client;
	private String mySymbol;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	public static SimpleClient getClient(String host, int port) {
		if (client == null) {
			client = new SimpleClient(host, port);
		}
		return client;
	}

	public static SimpleClient getClient() {
		return client;
	}

	public String getMySymbol() {
		return mySymbol;
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof gameMove) {
			EventBus.getDefault().post((gameMove) msg);
		} else if (msg instanceof String) {
			String m = (String) msg;
			if (m.startsWith("ASSIGN:")) {
				mySymbol = m.substring(7);
				System.out.println("Symbol: " + mySymbol);
			} else if (m.equals("start game")) {
				Platform.runLater(() -> {
					try {
						App.setRoot("primary");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			} else if (m.equals("Game is full")) {
				System.err.println("Game is full");
				Platform.exit();
			} else if (m.equals("Not your turn")) {
				System.err.println("Not your turn");
			} else {
				System.out.println("Server: " + m);
			}
		}
	}

	public void sendMove(gameMove move) {
		try {
			sendToServer(move);
		} catch (IOException e) {
			System.err.println("Send failed: " + e.getMessage());
		}
	}
}
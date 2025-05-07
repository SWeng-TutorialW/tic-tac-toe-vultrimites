package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.gameMove;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleServer extends AbstractServer {

	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	private String[][] board = new String[3][3];

	public SimpleServer(int port) {
		super(port);
		resetBoard();
	}

	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = "";
			}
		}
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof gameMove) {
			gameMove move = (gameMove) msg;
			System.out.printf("Received move: %s at (%d, %d)\n", move.getPlayerSymbol(), move.getRow(), move.getCol());

			// Update board
			board[move.getRow()][move.getCol()] = move.getPlayerSymbol();

			if (checkWin(move.getPlayerSymbol())) {
				broadcast(new gameMove(-1, -1, move.getPlayerSymbol() + " won!"));
				resetBoard();
			} else if (isBoardFull()) {
				broadcast(new gameMove(-1, -1, "Draw"));
				resetBoard();
			} else {
				broadcast(move);
			}

		} else if (msg.toString().startsWith("add client")) {
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);
			try {
				client.sendToClient("client added successfully");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (SubscribersList.size() == 2){
				broadcast("start game");
			}else{
				System.out.printf("Currently connected clients: %d. Waiting for one more...", SubscribersList.size());
			}

		} else if (msg.toString().startsWith("remove client")) {
			SubscribersList.removeIf(subscribedClient -> subscribedClient.getClient().equals(client));
		}
	}

	private void broadcast(Object message) {
		for (SubscribedClient sc : SubscribersList) {
			try {
				sc.getClient().sendToClient(message);
			} catch (IOException e) {
				System.err.println("Failed to send to client: " + e.getMessage());
			}
		}
	}

	private boolean checkWin(String symbol) {
		for (int i = 0; i < 3; i++) {
			if (symbol.equals(board[i][0]) && symbol.equals(board[i][1]) && symbol.equals(board[i][2])) return true;
			if (symbol.equals(board[0][i]) && symbol.equals(board[1][i]) && symbol.equals(board[2][i])) return true;
		}
		return symbol.equals(board[0][0]) && symbol.equals(board[1][1]) && symbol.equals(board[2][2])
				|| symbol.equals(board[0][2]) && symbol.equals(board[1][1]) && symbol.equals(board[2][0]);
	}

	private boolean isBoardFull() {
		for (String[] row : board) {
			for (String cell : row) {
				if (cell.isEmpty()) return false;
			}
		}
		return true;
	}

	public void sendToAllClients(String message) {
		for (SubscribedClient subscribedClient : SubscribersList) {
			try {
				subscribedClient.getClient().sendToClient(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

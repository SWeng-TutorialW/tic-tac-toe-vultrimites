package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.gameMove;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.*;

public class SimpleServer extends AbstractServer {

	private final List<ConnectionToClient> players = new ArrayList<>(2);
	private final Map<ConnectionToClient, String> symbols = new HashMap<>();
	private final String[][] board = new String[3][3];
	private int currentTurn = 0;

	public SimpleServer(int port) {
		super(port);
		resetBoard();
	}

	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			Arrays.fill(board[i], "");
		}
		currentTurn = 0;
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof gameMove) {
			handleGameMove((gameMove) msg, client);
		} else if (msg.toString().startsWith("add client")) {
			handleNewClient(client);
		} else if (msg.toString().startsWith("remove client")) {
			players.remove(client);
			symbols.remove(client);
		}
	}

	private void handleNewClient(ConnectionToClient client) {
		if (players.size() >= 2) {
			try {
				client.sendToClient("Game is full");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		String symbol = players.isEmpty() ? "X" : "O";
		players.add(client);
		symbols.put(client, symbol);

		try {
			// Send ASSIGN to all clients
			client.sendToClient("ASSIGN:" + symbol);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (players.size() == 2) {
			broadcast("start game");
		}
	}

	private void handleGameMove(gameMove move, ConnectionToClient client) {
		String sym = symbols.get(client);

		// 1) Validate it’s really this client’s symbol and turn
		if (!move.getPlayerSymbol().equals(sym)) {
			// wrong symbol → ignore
			return;
		}
		if (players.size() < 2 || !players.get(currentTurn).equals(client)) {
			// not your turn yet → ignore
			return;
		}

		int row = move.getRow();
		int col = move.getCol();
		if (!board[row][col].isEmpty()) {
			// cell already occupied → ignore
			return;
		}

		// 2) Apply move to server’s board
		board[row][col] = sym;

		// 3) Broadcast this move object to ALL connected clients
		broadcast(move);

		// 4) Check for end‐of‐game
		if (checkWin(sym)) {
			// announce winner to everyone
			broadcast(new gameMove(-1, -1, sym));
			resetBoard();
		}
		else if (isBoardFull()) {
			// announce draw
			broadcast(new gameMove(-1, -1, "DRAW"));
			resetBoard();
		}
		else {
			// 5) Switch turn and wait for next move
			currentTurn = 1 - currentTurn;
		}
	}

	private void broadcast(Object message) {
		List<ConnectionToClient> toRemove = new ArrayList<>();
		for (ConnectionToClient client : players) {
			try {
				client.sendToClient(message);
			} catch (IOException e) {
				e.printStackTrace();
				toRemove.add(client);
			}
		}
		players.removeAll(toRemove);
		symbols.keySet().removeAll(toRemove);
	}

	private boolean checkWin(String symbol) {
		for (int i = 0; i < 3; i++) {
			if (symbol.equals(board[i][0]) && symbol.equals(board[i][1]) && symbol.equals(board[i][2])) return true;
			if (symbol.equals(board[0][i]) && symbol.equals(board[1][i]) && symbol.equals(board[2][i])) return true;
		}
		return (symbol.equals(board[0][0]) && symbol.equals(board[1][1]) && symbol.equals(board[2][2])) ||
				(symbol.equals(board[0][2]) && symbol.equals(board[1][1]) && symbol.equals(board[2][0]));
	}

	private boolean isBoardFull() {
		for (String[] row : board) {
			for (String cell : row) {
				if (cell.isEmpty()) return false;
			}
		}
		return true;
	}
}
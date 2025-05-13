package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Arrays;
import java.util.List;
import il.cshaifasweng.OCSFMediatorExample.entities.gameMove;

public class PrimaryController {
	@FXML private Label winnerText;
	@FXML private Button topLeftButton, topMiddleButton, topRightButton;
	@FXML private Button middleLeftButton, middleButton, middleRightButton;
	@FXML private Button bottomLeftButton, bottomMiddleButton, bottomRightButton;
	private String mySymbol;
	private List<Button> buttons;

	@FXML
	public void initialize() {
		EventBus.getDefault().register(this);
		mySymbol = SimpleClient.getClient().getMySymbol();
		buttons = Arrays.asList(
				topLeftButton, topMiddleButton, topRightButton,
				middleLeftButton, middleButton, middleRightButton,
				bottomLeftButton, bottomMiddleButton, bottomRightButton
		);
		int[][] positions = {
				{0,0},{0,1},{0,2},
				{1,0},{1,1},{1,2},
				{2,0},{2,1},{2,2}
		};
		for (int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
			btn.setUserData(positions[i]);
			btn.setOnAction(this::handleCellClick);
		}
	}

	@FXML
	private void handleCellClick(ActionEvent event) {

		Button btn = (Button) event.getSource();
		if (btn.getText().isEmpty()) {
			System.out.println("Empty button");
			System.out.println(btn.getUserData());
			int[] pos = (int[]) btn.getUserData();
			gameMove move = new gameMove(pos[0], pos[1], mySymbol);
			SimpleClient.getClient().sendMove(move);
		}
	}

	@Subscribe
	public void onGameMove(gameMove move) {
		Platform.runLater(() -> {
			if (move.getRow() == -1) {
				String result = "DRAW".equals(move.getPlayerSymbol())
						? "Draw!"
						: move.getPlayerSymbol() + " wins!";
				winnerText.setText(result);
				buttons.forEach(b -> b.setDisable(true));
			} else {
				int idx = move.getRow() * 3 + move.getCol();
				Button btn = buttons.get(idx);
				btn.setText(move.getPlayerSymbol());
				btn.setDisable(true);
			}
		});
	}

	@FXML
	private void restartGame(ActionEvent e) {
		buttons.forEach(b -> {
			b.setText("");
			b.setDisable(false);
		});
		winnerText.setText("Tic-Tac-Toe");
	}
}
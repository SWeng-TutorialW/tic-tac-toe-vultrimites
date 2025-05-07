package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.gameMove;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.ArrayList;
import javafx.event.ActionEvent;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;

public class PrimaryController {

	@FXML private Button middleButton;
	@FXML private Button bottomLeftButton;
	@FXML private Button bottomMiddleButton;
	@FXML private Button bottomRightButton;
	@FXML private Text winnerText;
	@FXML private Button middleLeftButton;
	@FXML private Button middleRightButton;
	@FXML private Button restartButton;
	@FXML private AnchorPane rootPane;
	@FXML private Button topLeftButton;
	@FXML private Button topMiddleButton;
	@FXML private Button topRightButton;

	private int playerTurn = 0;
	ArrayList<Button> buttons;

	@FXML
	void initialize(URL url, ResourceBundle resourceBundle) {
		EventBus.getDefault().register(this);

		buttons = new ArrayList<>(Arrays.asList(
				topLeftButton, topMiddleButton, topRightButton,
				middleLeftButton, middleButton, middleRightButton,
				bottomLeftButton, bottomMiddleButton, bottomRightButton
		));

		int[][] positions = {
				{0,0}, {0,1}, {0,2},
				{1,0}, {1,1}, {1,2},
				{2,0}, {2,1}, {2,2}
		};

		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			button.setUserData(positions[i]);
			setupButton(button);
			button.setFocusTraversable(false);
		}
	}

	private void setupButton(Button button) {
		button.setOnMouseClicked(mouseEvent -> {
			if (button.getText().isEmpty()) {
				String symbol = (playerTurn % 2 == 0) ? "X" : "O";
				int[] pos = (int[]) button.getUserData();
				gameMove move = new gameMove(pos[0], pos[1], symbol);
				client.sendMove(move);
				playerTurn++;
			}
		});
	}

	@FXML
	public void restartGame(ActionEvent event) {
		buttons.forEach(this::resetButton);
	}

	public void resetButton(Button button) {
		button.setDisable(false);
		button.setText("");
		winnerText.setText("Tic-Tac-Toe");
	}

	@Subscribe
	public void onGameMove(gameMove move) {
		if (move.getRow() == -1 && move.getCol() == -1) {
			// Win or Draw message
			winnerText.setText(move.getPlayerSymbol());
			buttons.forEach(btn -> btn.setDisable(true));
		} else {
			int index = move.getRow() * 3 + move.getCol();
			Button button = buttons.get(index);
			button.setText(move.getPlayerSymbol());
			button.setDisable(true);
		}
	}

	public void stop() {
		EventBus.getDefault().unregister(this);
	}
}

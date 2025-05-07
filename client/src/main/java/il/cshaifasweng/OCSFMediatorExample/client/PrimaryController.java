package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class PrimaryController {

	@FXML
	private Button middleButton;

	@FXML
	private Button bottomLeftButton;

	@FXML
	private Button bottomMiddleButton;

	@FXML
	private Button bottomRightButton;

	@FXML
	private Text winnerText;

	@FXML
	private Button middleLeftButton;

	@FXML
	private Button middleRightButton;

	@FXML
	private Button restartButton;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Button topLeftButton;

	@FXML
	private Button topMiddleButton;

	@FXML
	private Button topRightButton;

	private int playerTurn = 0;

	ArrayList<Button> buttons;

	@FXML
	void initialize(URL url, ResourceBundle rescourceBundle) {
		buttons = new ArrayList<>(Arrays.asList(topLeftButton, topMiddleButton, topRightButton
		, middleLeftButton, middleRightButton, middleButton
		, bottomLeftButton, bottomMiddleButton, bottomRightButton, restartButton));

		buttons.forEach(button -> {
			setupButton(button);
			button.setFocusTraversable(false);
		});
	}

	@FXML
	void restartGame(ActionEvent event) {
		buttons.forEach(this::resetButton);
	}

	public void resetButton(Button button) {
		button.setDisable(false);
		button.setText("");
		winnerText.setText("Tic-Tac-Toe");
	}

	private void setupButton(Button button) {
		button.setOnMouseClicked(mouseEvent -> {
			setPlayerSymbol(button);
			button.setDisable(true);
			checkIfGameIsOver();
		});
	}

	public void setPlayerSymbol(Button button) {
		if(playerTurn % 2 == 0) {
			button.setText("X");
			playerTurn = 1;
		}
		else{
			button.setText("O");
			playerTurn = 0;
		}
	}

	public void checkIfGameIsOver(){
		for (int a = 0; a < buttons.size(); a++) {
			String line = switch(a){
				case 0 -> topLeftButton.getText() + topMiddleButton.getText() + topRightButton.getText();
				case 1 -> middleLeftButton.getText() + middleButton.getText() + middleRightButton.getText();
				case 2 -> bottomLeftButton.getText() + bottomMiddleButton.getText() + bottomRightButton.getText();
				case 3 -> topLeftButton.getText() + middleButton.getText() + bottomRightButton.getText();
				case 4 -> topRightButton.getText() + middleButton.getText() + bottomLeftButton.getText();
				case 5 -> topLeftButton.getText() + middleLeftButton.getText() + bottomLeftButton.getText();
				case 6 -> topMiddleButton.getText() + middleButton.getText() + bottomMiddleButton.getText();
				case 7 -> topRightButton.getText() + middleRightButton.getText() + bottomRightButton.getText();
				default -> null;
			};
		// X wins
		if (line.equals("XXX")){
			winnerText.setText("X won!");
		} else if(line.equals("OOO")) {
			winnerText.setText("O won!");
			}
		}
	}
}

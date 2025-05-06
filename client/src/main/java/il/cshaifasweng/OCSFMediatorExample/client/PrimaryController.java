package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    void sendWarning(ActionEvent event) {
    	try {
			SimpleClient.getClient("", 3000).sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@FXML
	void initialize(){
		try {
			SimpleClient.getClient("", 3000).sendToServer("add client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package simonhanna.ense480.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simonhanna.ense480.models.User;
import simonhanna.ense480.services.DatabaseService;

public class LandingController extends KeyMetricController implements Initializable {
	
	@FXML
	private Text selectUserLabel;
	@FXML
	private Button selectUserButton;
	@FXML
	private ComboBox<User> userComboBox;
	@FXML
	private Button addUserButton;
	@FXML
	private Text errorText;
	@FXML
	private Text addMetricError;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initKeyMetrics(detectUserKeyMetrics);
		this.startTime = (double)System.nanoTime()/1000000000.0;
		DatabaseService.getUserAliases().forEach(user -> {
			userComboBox.getItems().add(user);
		});
	}
	
	@FXML
	public void addUser() {
		try {
			writeContents();
			Stage stage = (Stage) addUserButton.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddUserView.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@FXML
	public void selectUser() {
		try {
			User user = userComboBox.getValue();
			
			if(user == null) {
				errorText.setText("You must select a user");
				return;
			}
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProfileView.fxml"));
			Stage stage = (Stage) selectUserButton.getScene().getWindow();
			stage.setScene(new Scene(
				      (Parent) loader.load()
				    ));
		
			ProfileController controller = loader.getController();
			controller.initializeController(user);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@FXML
	public void addDetectUserKeyMetric(KeyEvent k) {
		addKeyMetric(k, detectUserKeyMetrics);
	}
	
	public void writeContents() {
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				detectUserKeyMetrics[i][j].writeContents();
			}
		}
	}
}

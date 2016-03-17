package simonhanna.ense480.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simonhanna.ense480.models.KeyMetric;
import simonhanna.ense480.models.Profile;
import simonhanna.ense480.models.User;
import simonhanna.ense480.services.DatabaseService;
import simonhanna.ense480.services.NeuralNetworkService;

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
	@FXML
	private TextField identifyUserText;
	@FXML
	private TextArea detectUserInput;
	
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
	public void detectUser() {
		List<KeyMetric> metricList = new ArrayList<KeyMetric>();
		
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				metricList.add(detectUserKeyMetrics[i][j]);
			}
		}
		Profile detectedUser = NeuralNetworkService.identifyUser(metricList);
		identifyUserText.setText(detectedUser.getUser().getAlias() + " - " + detectedUser.getProfilename());
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
	
	@FXML
	public void resetMetrics() {
		initKeyMetrics(detectUserKeyMetrics);
		detectUserInput.setText("");
	}
	
	public void writeContents() {
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				detectUserKeyMetrics[i][j].writeContents();
			}
		}
	}
}

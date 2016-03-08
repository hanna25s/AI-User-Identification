package simonhanna.ense480.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import simonhanna.ense480.models.Profile;
import simonhanna.ense480.models.User;
import simonhanna.ense480.services.DatabaseService;

public class ProfileController extends KeyMetricController implements Initializable{

	@FXML
	ComboBox<Profile> profileComboBox;
	@FXML
	TextField profileName;
	@FXML
	Text addProfileError;
	@FXML
	Text saveMetricsError;
	
	private User currentUser;
	private Profile currentProfile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initKeyMetrics(detectUserKeyMetrics);
		currentProfile = null;
	}
	
	public void initializeController(User user) {
		System.out.println("Initializing controller");
		this.currentUser = user;
		
		if(!currentUser.getProfiles().isEmpty()) {
			currentUser.getProfiles().forEach(profile -> {
				profileComboBox.getItems().add(profile);
			}); 
		}
	}

	@FXML
	public void selectUser() {
		currentProfile = profileComboBox.getValue();
	}
	
	@FXML
	public void saveMetrics() {
		if(currentProfile == null) {
			saveMetricsError.setText("You must select a profile");
			return;
		}
		DatabaseService.addKeyMetrics(currentProfile, alterProfileKeyMetrics);
	}
	
	@FXML
	public void addProfile() {
		String newProfileName = profileName.getText();
		
		if(newProfileName == null || newProfileName.equals("")) {
			addProfileError.setText("You must enter a profile name");
			return;
		}
		
		addProfileError.setText("");
		DatabaseService.addProfile(profileName.getText(), currentUser);
		
		profileComboBox = new ComboBox<Profile>();
		currentUser.getProfiles().forEach(profile -> {
			profileComboBox.getItems().add(profile);
		});
	}
	
	@FXML
	public void addProfileKeyMetric(KeyEvent k) {
		if(currentProfile == null) {
			saveMetricsError.setText("You must select a profile first");
			return;
		}
		saveMetricsError.setText("");
		addKeyMetric(k,alterProfileKeyMetrics);
	}
	
	@FXML
	public void addDetectUserKeyMetric(KeyEvent k) {
		addKeyMetric(k, detectUserKeyMetrics);
	}
}

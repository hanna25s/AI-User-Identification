package simonhanna.ense480.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import simonhanna.ense480.models.KeyMetric;
import simonhanna.ense480.models.Profile;
import simonhanna.ense480.models.User;
import simonhanna.ense480.services.DatabaseService;
import simonhanna.ense480.services.NeuralNetworkService;

public class ProfileController extends KeyMetricController implements Initializable{

	@FXML
	ComboBox<Profile> profileComboBox;
	@FXML
	ComboBox<User> userComboBox;
	@FXML
	TextField profileName;
	@FXML
	Text addProfileError;
	@FXML
	Text saveMetricsError;
	@FXML
	TextField newUserInput;
	@FXML
	Text changeUserError;
	@FXML
	TextField currentUserText;
	@FXML
	TextField identifyUserText;
	@FXML
	TextArea alterProfileMetricInput;
	@FXML
	TextField profileInputStatus;
	
	private User currentUser;
	private Profile currentProfile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initKeyMetrics(detectUserKeyMetrics);
		initKeyMetrics(alterProfileKeyMetrics);
		DatabaseService.getUserAliases().forEach(user -> {
			userComboBox.getItems().add(user);
		});
		currentProfile = null;
	}
	
	/**
	 * Used to initialize the controller with variables from the previous view
	 * 
	 * @param user The user selected on the landing view
	 */
	public void initializeController(User user) {
		System.out.println("Initializing controller");
		this.currentUser = user;
		currentUserText.setText(currentUser.getAlias());
		
		if(currentUser.getProfiles() != null && !currentUser.getProfiles().isEmpty()) {
			currentUser.getProfiles().forEach(profile -> {
				profileComboBox.getItems().add(profile);
			}); 
		}
	}

	/**
	 * Changes currently selected user
	 */
	@FXML
	public void changeUser() {
		if(userComboBox.getValue() == null) {
			changeUserError.setText("You must select a user first");
			return;
		} else {
			currentUser = userComboBox.getValue();
			currentUserText.setText(currentUser.getAlias());
			
			profileComboBox.getItems().clear();
			currentUser.getProfiles().forEach(prof -> {
				profileComboBox.getItems().add(prof);
			});
		}
	}
	
	
	/**
	 * Adds a new user to the database then reloads the combo box
	 */
	@FXML
	public void addUser() {
		if(newUserInput.getText() == null || newUserInput.getText().equals("")) {
			changeUserError.setText("You must enter a username");
			return;
		}
		DatabaseService.addUser(newUserInput.getText());
		
		userComboBox.getItems().clear();
		DatabaseService.getUserAliases().forEach(user -> {
			userComboBox.getItems().add(user);
		});
	}
	
	/**
	 * Used to save any new metrics that have been entered, then reset the alterProfileMetricInput
	 * array. In order for the metrics to be saved, the user must have chosen a valid profile. After
	 * updating the metrics, this code then retrains all neural networks with the new metrics. 
	 */
	@FXML
	public void saveMetrics() {	
		currentProfile = profileComboBox.getValue();
		
		if(currentProfile == null) {
			saveMetricsError.setText("You must select a profile");
			return;
		}
		
		for(int i=0; i<10; i++) {
			for(int j=0 ;j<10; j++) {
				alterProfileKeyMetrics[i][j].setProfile(currentProfile);
			}
		}
		
		profileInputStatus.setText("Training");
		alterProfileMetricInput.setText("");
		
		//Run on a new thread to avoid slowing the UI down.
		new Thread() {
            public void run() {
            	DatabaseService.updateKeyMetrics(currentProfile, alterProfileKeyMetrics);
        		initKeyMetrics(alterProfileKeyMetrics);
            	NeuralNetworkService.trainAllNeuralNetworks();
            	Platform.runLater(new Runnable() {
					@Override
					public void run() {
						profileInputStatus.setText("Ready");
					}	
            	});
            }
        }.start();
	}
	
	/**
	 * Adds a new profile for the currently selected user
	 */
	@FXML
	public void addProfile() {
		String newProfileName = profileName.getText();
		
		if(newProfileName == null || newProfileName.equals("")) {
			addProfileError.setText("You must enter a profile name");
			return;
		}
		
		addProfileError.setText("");
		DatabaseService.addProfile(profileName.getText(), currentUser);
		profileComboBox.getItems().clear();
		
		currentUser.getProfiles().forEach(profile -> {
			profileComboBox.getItems().add(profile);
		});
	}
	
	/**
	 * Converts the metric array to a list, then calls the NeuralNetwork service to identify the user
	 */
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
	
	/**
	 * Used to add metrics for training a profile's neural network
	 * 
	 * @param k  Used to determine what key was hit
	 */
	@FXML
	public void addProfileKeyMetric(KeyEvent k) {
		addKeyMetric(k, alterProfileKeyMetrics);
	}
	
	/**
	 * Used to add metrics for detecting the user
	 * 
	 * @param k  Used to determine what key was hit
	 */
	@FXML
	public void addDetectUserKeyMetric(KeyEvent k) {
		addKeyMetric(k, detectUserKeyMetrics);
	}
	
	@FXML
	public void resetMetrics() {
		System.out.println("Resetting metrics");
		initKeyMetrics(detectUserKeyMetrics);
	}
}

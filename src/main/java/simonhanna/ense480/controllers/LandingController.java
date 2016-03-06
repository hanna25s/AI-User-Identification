package simonhanna.ense480.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
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
import simonhanna.ense480.models.KeyMetric;
import simonhanna.ense480.models.Profile;
import simonhanna.ense480.models.User;
import simonhanna.ense480.services.DatabaseService;

public class LandingController implements Initializable{

	private static KeyMetric[][] keyMetrics = new KeyMetric[10][10];
	private static Profile profile;
	
	private char newKey;
	private char oldKey;
	private double startTime, endTime, timeBetweenKeys;
	
	@FXML
	private Text selectUserLabel;
	@FXML
	private Button selectUserButton;
	@FXML
	private ComboBox<User> userComboBox;
	@FXML
	private Button addUserButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initKeyMetrics();
		this.startTime = (double)System.nanoTime()/1000000000.0;
		DatabaseService.getUserAliases().forEach(user -> {
			userComboBox.getItems().add(user);
		});
	}
	
	@FXML
	public void addUser(ActionEvent event) {
		try {
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
	public void selectUser(ActionEvent event) {
		System.out.println("In selectUserButton");
	}
	
	@FXML
	public void keyTyped(KeyEvent k) {
		endTime = (double)System.nanoTime()/1000000000.0;
    	newKey = k.getCharacter().charAt(0);
    	timeBetweenKeys = (endTime - startTime);
    	
    	//In case the user leaves and comes back. Avoid incredibly large values as they lead to errors.
    	if(timeBetweenKeys > 10.0) {
    		startTime = endTime;
            oldKey = newKey;
    		return;
    	}
    	
    	KeyMetric keyMetric = keyMetrics[getKeyGroup(oldKey)][getKeyGroup(newKey)];
    	
    	keyMetric.setTime(keyMetric.getTime() + timeBetweenKeys);
    	keyMetric.setNumberOfOccurences(keyMetric.getNumberOfOccurences() + 1);

        startTime = endTime;
        oldKey = newKey;
	}
	
	public static void initKeyMetrics() {
    	for(int i=0; i<10; i++) {
    		for(int j=0; j<10; j++) {
    			keyMetrics[i][j] = new KeyMetric(i, j, profile);
    		}
    	}
    }
    
	/**
	 * getKeyGroup
	 * Input: Key entered by user
	 * Returns: Group input key belongs to
	 * Description: This is a helped method that is used
	 *      to get the key group for each key entered.
	 **/
	public int getKeyGroup(char key) {
		
		key = Character.toUpperCase(key);
		
		switch(key) {
		case 'Q':
		case 'A':
		case 'Z':
			return 0;
		case 'W':
		case 'S':
		case 'X':
			return 1;
		case 'C':
		case 'D':
		case 'E':
			return 2;
		case 'R':
		case 'T':
		case 'F':
		case 'G':
		case 'V':
		case 'B':
			return 3;
		case 'Y':
		case 'U':
		case 'H':
		case 'J':
		case 'N':
		case 'M':
			return 4;
		case 'I':
		case 'K':
			return 5;
		case 'O':
		case 'L':
			return 6;
		case 'P':
			return 7;
		case ' ':
			return 8;
		default:
			return 9;
		}
	}
}

package simonhanna.ense480.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import simonhanna.ense480.services.DatabaseService;

public class AddUserController implements Initializable{

	@FXML
	private TextField newUserInput;
	@FXML
	private Button backButton;
	@FXML
	private Button addUserButton;
	@FXML
	private Text addUserError;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	/**
	 * Returns user to the landing view
	 * 
	 * @param event Contains information of previous view
	 */
	@FXML
	public void back(ActionEvent event) {
		try {
			Stage stage = (Stage) backButton.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/view/LandingView.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new user to the database then returns to the landing view
	 * 
	 * @param event
	 */
	@FXML
	public void addUser(ActionEvent event) {
		try {
			String userAlias = newUserInput.getText();
			if(userAlias == null || userAlias.equalsIgnoreCase("")) {
				System.out.println("User was null");
				addUserError.setText("You must enter a username");
				return;
			}
			
			DatabaseService.addUser(userAlias);
			
			Stage stage = (Stage) addUserButton.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/view/LandingView.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

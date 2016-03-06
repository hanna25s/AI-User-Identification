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
import javafx.stage.Stage;

public class AddUserController implements Initializable{

	@FXML
	private TextField newUserInput;
	@FXML
	private Button backButton;
	@FXML
	private Button addUserButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
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
	
	@FXML
	public void addUser(ActionEvent event) {
		try {
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

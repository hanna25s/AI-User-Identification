package simonhanna.ense480.services;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
    	launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/LandingView.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("AI User Identification");
		    primaryStage.setOnCloseRequest(e -> Platform.exit());
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }
}

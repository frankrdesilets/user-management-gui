package frankdesilets.User_Manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class contains the main method of the application. Various JavaFX
 * methods are configured to launch the user interface.
 */
public class UserManager extends Application {

	/*
	 * The single scene of the UI.
	 * The controller class is Controller.
	 */
	private static Scene scene;

	/**
	 * The main entry point for the application. The start method is called after
	 * the init method has returned, and after the system is ready for the
	 * application to begin running.
	 */
	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML("main")); // the scene is loaded from main.fxml, located in src/main/resources
		stage.setScene(scene); // the scene is added to the stage
		stage.setResizable(false); // the stage is set to be non-resizable
		stage.setTitle("User Manager"); // the title of the stage is set to "User Manager"
		stage.show(); // the stage is set to be visible
	}

	/**
	 * Sets the root of the scene.
	 * 
	 * @param fxml
	 * @throws IOException
	 */
	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	/**
	 * Loads an object hierarchy from a FXML document.
	 * 
	 * @param fxml
	 * @return
	 * @throws IOException
	 */
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(UserManager.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	/**
	 * The main method of the application. Launches the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch();
	}

}
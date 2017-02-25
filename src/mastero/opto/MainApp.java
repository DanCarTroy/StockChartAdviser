package mastero.opto;

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import mastero.opto.model.User;
import mastero.opto.view.LoginController;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class MainApp extends Application {

	private Stage primaryStage;
	private StackPane loginForm;

	public static User currentUser;

	/**
	 * Default Constructor. Could be used later to initialize values.
	 */
	public MainApp()
	{

	}

	@Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StockChartAdviser");

        initLogin();


    }

	/**
     * Initializes the login form.
     */
    public void initLogin() {
        try {
            // Load login form from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Login.fxml"));
            loginForm = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(loginForm);
            primaryStage.setScene(scene);
            primaryStage.show();

         // Give the controller access to the main app.
            LoginController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
		launch(args);
	}
}

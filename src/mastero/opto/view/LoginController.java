package mastero.opto.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import mastero.opto.MainApp;
import mastero.opto.model.User;
import mastero.opto.util.DBConnection;


import java.sql.ResultSet;
import java.sql.SQLException;



public class LoginController {

	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	// Reference to the main application.
    private MainApp mainApp;

	public LoginController() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin()
    {
    	String username = usernameField.getText();
		String password = passwordField.getText();
		User current = verifyLogin(username, password);
		if (current != null) {
			usernameField.getStyleClass().remove("error");
			passwordField.getStyleClass().remove("error");
			MainApp.currentUser = current;
			/* mainApp.showPersonEditDialog(tempPerson);
			Stage stage = Application.getStage(e);
			ViewLoader v = new ViewLoader(Constants.FXML_MainPage);
			stage.setScene(new Scene(v.getParent())); */

			// Testing login
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Login Successful");
            alert.setHeaderText("User is valid an exists in the DB");
            alert.setContentText("You logged in successfully. Congrats!");

            alert.showAndWait();
		}else {

			usernameField.getStyleClass().add("error");
			passwordField.getStyleClass().add("error");

			Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid user or password");
            alert.setContentText("Please try entering again!");
            alert.showAndWait();

		}
    }

    public User verifyLogin(String username, String password) {
		try {
			DBConnection newConnection = new DBConnection();

			return getLogin(username, password, newConnection);
		} catch (SQLException e) {
			// Have to implement an AlertBox class to avoid re-typing alerts.
			//AlertBox.showError("Something went wrong when verifying your login.");


			// This should be an alert for an SQL exception
			/*Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Login Failed");
            alert.setHeaderText("Invalid user or password");
            alert.setContentText("Please try entering again!");
			e.printStackTrace(); */
		}
		return null;
	}

    /**
	 * Retrieves a user from the DB from the given username and
	 * password. Missing MD5 for storing password, will implement later.
	 * Thinking about putting this method inside DBConnection instead.
	 *
	 * @return A valid User if username and password are valid, null otherwise.
	 */
	public static User getLogin(String username, String password, DBConnection myCon) throws SQLException {
		ResultSet rs = myCon.executeQuery("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'", "user",
				"user_login", username, "user_pass", password);

		if (rs.next()) {
			//String id = rs.getString();
			String firstname = rs.getString("first_name");
			String lastname = rs.getString("last_name");
			String email = rs.getString("user_email");
			return new User(username, firstname, lastname, email);
		}

		rs.close();

		return null;
	}

}

package mastero.opto.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import mastero.opto.MainApp;

public class MainFrameController {

	// Reference to the main application
    MainApp mainApp = new MainApp();

    public MainFrameController() {
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

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
       // mainApp.getPersonData().clear();
       // mainApp.setUserFilePath(null);
    }

    /**
     * This feature will be implemented in Iteration 3..
     * Opens a FileChooser to display data of user's csv file.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {

            //mainApp.setCurrentStock();
        }
    }

    /**
     * Saves a screenshot to the location that was previously used to save. If there is no
     * previous location, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File file = mainApp.getLastOpenedFile();
        if (file != null) {
            mainApp.saveScreenshotToFile(file);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".png")) {
                file = new File(file.getPath() + ".png");
            }
            mainApp.saveScreenshotToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("StockChartAdviser");
        alert.setHeaderText("About");
        alert.setContentText("Author: Comp354 Team10 - Concordia University - Montreal, Canada\nWebsite: https://github.com/DanCarTroy/StockChartAdviser");

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

}

package mastero.opto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mastero.opto.model.User;
import mastero.opto.view.CreateUserDialogController;
import mastero.opto.view.LoginController;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class MainApp extends Application {

	private Stage primaryStage;
	private StackPane loginForm;
	private static BorderPane mainLayout = new BorderPane();

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
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showCreateUserDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CreateUserDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create New User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Passing the stage to the controller so that a custom button can close the stage.
            CreateUserDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            //controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showMainWindow() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
		mainLayout = loader.load();


		// Show the scene containing the root layout.
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

	}


	public static void showChartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/LineChartView.fxml"));
		BorderPane chartView = loader.load();
		mainLayout.setCenter(chartView);
	}

	public XYChart.Series<String, Number> getChartData(){
		Scanner csvFile;
		DecimalFormat f = new DecimalFormat("###.##");
		ArrayList<Double> closePrice = new ArrayList<>();
		ArrayList<String> dateStrings = new ArrayList<>();
		int dateStringIndex = 0;
		int closePriceIndex = 4;
		try{
			csvFile = new Scanner(new File("src/mastero/opto/view/Sample data.csv"));
			csvFile.useDelimiter(",");
			csvFile.nextLine();
			while(csvFile.hasNext()){
				String dataString = csvFile.nextLine();
				closePrice.add(Double.parseDouble((f.format(Double.parseDouble(dataString.split(",")[closePriceIndex])))));
				dateStrings.add(dataString.split(",")[dateStringIndex]);
			}
		}
		catch(FileNotFoundException e){
			e.getStackTrace();
		}

		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
		for (int i = closePrice.size() - 1; i > -1;i--) {
			series.getData().add(new XYChart.Data<String, Number>(dateStrings.get(i), closePrice.get(i)));
		}
		return series;
	}

	public XYChart.Series<String, Number> getChartData(int range){
		Scanner csvFile;
		DecimalFormat f = new DecimalFormat("###.##");
		ArrayList<Double> closePrice = new ArrayList<>();
		ArrayList<String> dateStrings = new ArrayList<>();
		int dateStringIndex = 0;
		int closePriceIndex = 4;
		try{
			csvFile = new Scanner(new File("src/mastero/opto/view/Sample data.csv"));
			csvFile.useDelimiter(",");
			csvFile.nextLine();
			while(csvFile.hasNext()){
				String dataString = csvFile.nextLine();
				closePrice.add(Double.parseDouble((f.format(Double.parseDouble(dataString.split(",")[closePriceIndex])))));
				dateStrings.add(dataString.split(",")[dateStringIndex]);
			}
		}
		catch(FileNotFoundException e){
			e.getStackTrace();
		}

		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
		for (int i = range; i > -1;i--) {
			series.getData().add(new XYChart.Data<String, Number>(dateStrings.get(i), closePrice.get(i)));
		}
		return series;
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

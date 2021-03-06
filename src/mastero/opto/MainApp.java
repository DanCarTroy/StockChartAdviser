package mastero.opto;

import org.apache.commons.io.FileUtils;
import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mastero.opto.model.Stock;
import mastero.opto.model.User;
import mastero.opto.util.StockDownloader;
import mastero.opto.view.CreateUserDialogController;
import mastero.opto.view.LineChartController;
import mastero.opto.view.LoginController;
import mastero.opto.view.MainFrameController;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;




public class MainApp extends Application {

	private Stage primaryStage;
	private StackPane loginForm;
	private AnchorPane chartView;
	private static BorderPane mainLayout = new BorderPane();
	private LineChartController currentChartInstance;


	public static User currentUser;

	/**
     * The data as an observable list of Stocks.
     */
    private ObservableList<Stock> stockList = FXCollections.observableArrayList();

    private ObservableList<Stock> dow30List = FXCollections.observableArrayList();

    private ObservableList<Stock> mostActiveList = FXCollections.observableArrayList();

	/**
	 * Default Constructor. Could be used later to initialize values.
	 */
	public MainApp()
	{
		// Add some sample data -- Will replace this with favorite stocks from the the user
		stockList.add(new Stock("YHOO", "Yahoo"));
		stockList.add(new Stock("AAPL", "Apple"));
		stockList.add(new Stock("MSFT", "Microsoft"));
		stockList.add(new Stock("TSLA", "Tesla Motors"));
		stockList.add(new Stock("SNE", "Sony"));
		stockList.add(new Stock("NTDOY", "Nintendo"));
		stockList.add(new Stock("FCAU", "Fiat Chrysler"));
		stockList.add(new Stock("BDRBF", "Bombardier"));

		getDow30();

		try{
		getMostActive();
		}
		catch(JSONException e)
		{
			System.out.println("Could not get most active data. NASDAQ servers are busy :(");
		}
	}

	private void getMostActive() throws org.json.JSONException {
		// TODO Auto-generated method stub
		ArrayList<String> stockNames = new ArrayList<String>();
		ArrayList<String> stockSymbols = new ArrayList<String>();

		// YQL (Yahoo Query Language) url --> preparing the url --> Getting the name.

		String nameYqlUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20title%20from%20html%20where%20url%3D'http%3A%2F%2Fmoney.cnn.com%2Fdata%2Fhotstocks%2F'%20and%20xpath%3D'%2F%2Ftable%5B%40class%3D%22wsod_dataTable%20wsod_dataTableBigAlt%22%5D%2F%2Ftd%2Fspan'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		String symbolYqlUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20content%20from%20html%20where%20url%3D'http%3A%2F%2Fmoney.cnn.com%2Fdata%2Fhotstocks%2F'%20and%20xpath%3D'%2F%2Ftable%5B%40class%3D%22wsod_dataTable%20wsod_dataTableBigAlt%22%5D%2F%2Ftd%2Fa'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";


		String JSONstring = StockDownloader.getJASON(nameYqlUrl);
		System.out.println("get most active jason = "+JSONstring);

		String JSONsymbol = StockDownloader.getJASON(symbolYqlUrl);
		System.out.println("get most active jason = "+JSONsymbol);

		JSONArray arr, arr2 = null;
		try{

			JSONObject j = new JSONObject(JSONstring);
	        arr = j.getJSONObject("query").getJSONObject("results").getJSONArray("span");

	        JSONObject j2 = new JSONObject(JSONsymbol);
	        arr2 = j2.getJSONObject("query").getJSONObject("results").getJSONArray("a");



	        for(int i=0; i < arr.length(); i++)
	        {
	        	JSONObject obj = arr.getJSONObject(i);
	            String name = obj.getString("title");

	            String symbol = arr2.getString(i);

	            System.out.println(name);
	            stockNames.add(name);

	            System.out.println(symbol);
	            stockSymbols.add(symbol);

	            mostActiveList.add(new Stock(symbol, name));



	        }
		}
		catch(JSONException e){
			e.printStackTrace();
		}


	}

	private void getDow30() {
		// TODO Auto-generated method stub

		ArrayList<String> stockNames = new ArrayList<String>();
		ArrayList<String> stockSymbols = new ArrayList<String>();

		// YQL (Yahoo Query Language) url --> preparing the url --> Getting the name.
		String nameYqlUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20content%20from%20html%20where%20url%3D'http%3A%2F%2Fmoney.cnn.com%2Fdata%2Fdow30%2F'%20and%20xpath%3D'*%2F%2Ftd%5B%40class%3D%22wsod_firstCol%22%5D%2Fspan'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		String symbolYqlUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20content%20from%20html%20where%20url%3D'http%3A%2F%2Fmoney.cnn.com%2Fdata%2Fdow30%2F'%20and%20xpath%3D'%2F%2Ftable%2F*%5Bcontains(.%2C%22Company%22)%5D%2F%2Fa'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		String JSONstring = StockDownloader.getJASON(nameYqlUrl);
		System.out.println("get most dow30 jason(names) = "+JSONstring);

		String JSONsymbol = StockDownloader.getJASON(symbolYqlUrl);
		System.out.println("get dow30 jason(symbols) = "+JSONsymbol);

		JSONArray arr, arr2 = null;
		try{

			JSONObject j = new JSONObject(JSONstring);
	        arr = j.getJSONObject("query").getJSONObject("results").getJSONArray("span");

	        JSONObject j2 = new JSONObject(JSONsymbol);
	        arr2 = j2.getJSONObject("query").getJSONObject("results").getJSONArray("a");


	        //
	        for(int i=0; i < arr2.length(); i++)
	        {

	            String name = arr.getString(i);
	            String symbol = arr2.getString(i);

	            System.out.println(name);
	            stockNames.add(name);

	            System.out.println(symbol);
	            stockSymbols.add(symbol);

	            dow30List.add(new Stock(symbol, name));



	        }
		}
		catch(JSONException e){
			e.printStackTrace();
		}


	}

	/**
     * Returns the data as an observable list of Stocks.
     * Favorite stocks from the user.
     * @return
     */
    public ObservableList<Stock> getStockList() {
        return stockList;
    }

    public ObservableList<Stock> getDow30List() {
        return dow30List;
    }

    public ObservableList<Stock> getMostActiveList() {
        return mostActiveList;
    }


	@Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("StockChartAdviser");

     // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/aquicon_basecamp.png"));

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

    public void showMainFrame()throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/MainFrame.fxml"));
		setMainLayout((BorderPane) loader.load());
		Scene scene = new Scene(getMainLayout());
		primaryStage.setScene(scene);
		primaryStage.show();

		// Give the controller access to this LineChartController.
        MainFrameController controller2 = loader.getController();
        controller2.setMainApp(this);

		showChartView();


	}

    public void showMainWindow() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
		//mainLayout = loader.load();
		getMainLayout().setCenter(loader.load());


		// Show the scene containing the root layout.
        /*
		Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
		*/

	}


    public void showChartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/LineChartView.fxml"));
		chartView = loader.load();
		getMainLayout().setCenter(chartView);

		// Give the controller access to the main app.
        LineChartController controller = loader.getController();
        controller.setMainApp(this);

        currentChartInstance = controller;


	}

    public void saveScreenshot(File file)
    {
    	currentChartInstance.saveScreenshotToFile(file);
    	//currentChartInstance.resetGraph();
    }


    /**
     * Returns the file that was last opened.
     * This is read from the user's registry.
     * Null will be returned if nothing is found.
     *
     * @return last opened file
     */
    public File getLastOpenedFile() {

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null)
        	return new File(filePath);
        else
            return null;

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

	public static BorderPane getMainLayout() {
		return mainLayout;
	}

	public static void setMainLayout(BorderPane mainLayout) {
		MainApp.mainLayout = mainLayout;
	}
}

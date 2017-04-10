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
	XYChart.Series<String,Number> currentDataSeries = new XYChart.Series<String, Number>();

	public static User currentUser;

	/**
	 * Default Constructor. Could be used later to initialize values.
	 */
	public MainApp()
	{
		currentDataSeries = getCurrentSeries();
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

    public void showMainFrame()throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/MainFrame.fxml"));
		mainLayout = (BorderPane) loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();

		showMainWindow();
	}

    public void showMainWindow() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
		//mainLayout = loader.load();
		mainLayout.setCenter(loader.load());


		// Show the scene containing the root layout.
        /*
		Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
		*/

	}


    public static void showChartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/LineChartView.fxml"));
		AnchorPane chartView = loader.load();
		mainLayout.setCenter(chartView);
	}


    public XYChart.Series<String, Number> getChartData(){
		Scanner csvFile;
		DecimalFormat f = new DecimalFormat("###.##");
		ArrayList<Double> closePrice = new ArrayList<>();
		ArrayList<String> dateStrings = new ArrayList<>();
		int dateStringIndex = 0;
		int closePriceIndex = 4;

		getCSVFromYahoo();
		try{
			csvFile = new Scanner(new File("src/mastero/opto/view/tmpData.csv"/*"src/mastero/opto/view/Sample data.csv"*/));
			csvFile.useDelimiter(",");
			csvFile.nextLine();
			while(csvFile.hasNext()){
				String dataString = csvFile.nextLine();
				closePrice.add(Double.parseDouble((f.format(Double.parseDouble(dataString.split(",")[closePriceIndex])))));
				dateStrings.add(dataString.split(",")[dateStringIndex]);
			}
			csvFile.close();
		}
		catch(FileNotFoundException e){
			e.getStackTrace();
		}
		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
		for (int i = closePrice.size() - 1; i > -1;i--) {
			series.getData().add(new XYChart.Data<String, Number>(dateStrings.get(i), closePrice.get(i)));
		}
		currentDataSeries = copySeries(series);
		return series;
    }

    public XYChart.Series<String, Number> getChartData(int range){

		DecimalFormat f = new DecimalFormat("###.##");

		LocalDate fromDate = LocalDate.now(ZoneId.of( "America/Montreal")).minusDays(range);
		int year = fromDate.getYear();
		int month = fromDate.getMonthValue();
		int day = fromDate.getDayOfMonth();

		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();

		// YQL (Yahoo Query Language) url --> preparing the url with the range and stock that we want
		String yqlUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20date%2C%20close%20from%20csv%20where%20url%3D'http%3A%2F%2Fichart.finance.yahoo.com%2Ftable.csv%3Fs%3D"
		+"AAPL"+"%26a%3D"+month+"%26b%3D"+day+"%26c%3D"+year+"'%20and%20columns%3D'date%2Copen%2Chigh%2Clow%2Cclose%2Cvolume%2Cadj_close'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		String JSONstring = getJASON(yqlUrl);
		System.out.println("my jason = "+JSONstring);

		JSONArray arr = null;
		try{

			JSONObject j = new JSONObject(JSONstring);
	        arr = j.getJSONObject("query").getJSONObject("results").getJSONArray("row");


	        // Ending the loop before going through index 0 because index 0 of arr gives back the name of the attributes and not the values.
	        for(int i=arr.length()-1; i > 0; i--)
	        {
	            JSONObject obj = arr.getJSONObject(i);
	            Double closePrice = Double.parseDouble((f.format(Double.parseDouble(obj.getString("close")))));
	            String dateString = obj.getString("date");

	            series.getData().add(new XYChart.Data<String, Number>(dateString, closePrice));

	        }
		}
		catch(JSONException e){
			e.printStackTrace();
		}

		if (series == null){
			System.out.println("Null");
		}
		else
			System.out.println(series.getData().size());
		currentDataSeries = copySeries(series);
		System.out.println(currentDataSeries.getData().size());
		return series;
	}

    public static XYChart.Series<String, Number> copySeries(XYChart.Series<String, Number> target){
		XYChart.Series<String, Number> copy = new XYChart.Series<String, Number>();
		int size = target.getData().size();
		for(int i = 0; i < size; i++){
			copy.getData().add(target.getData().get(i));
		}
		return copy;
	}

    public XYChart.Series<String, Number> getCurrentSeries(){
		return copySeries(currentDataSeries);
	}

    public static void getCSVFromYahoo()
    {
    	URL source = null;
		try {
			source = new URL("http://ichart.finance.yahoo.com/table.csv?s=AAPL&ignore=.csv");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	File tmpFile = new File("src/mastero/opto/view/tmpData.csv");
    	try {
			FileUtils.copyURLToFile(source, tmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static String getJASON(String url)
    {
    	URLConnection connection = null;
    	URL source = null;
    	String JSONstring = null;

    	try{
    		source = new URL(url);
    	} catch(MalformedURLException e){
    		e.printStackTrace();
    	}

		try {
			connection = source.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			JSONstring = in.readLine();
			// Not necessary because it is returning just one line.
			/*
			String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        {
	            System.out.println(inputLine);
	        }
	        */
			//System.out.println("json is: ");
			//System.out.println(JSONstring);
			//System.out.println("after jason");
	        in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return JSONstring;

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

package mastero.opto.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Collections.reverse;

public class MainApp extends Application {
	private Stage primaryStage = new Stage();
	private static BorderPane mainLayout = new BorderPane();

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ProfitUs");
		showMainFrame();
		showMainWindow();
	}

	public void showMainFrame()throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("MainFrame.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void showMainWindow() throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("MainWindow.fxml"));
		mainLayout.setCenter(loader.load());
	}


	public static void showChartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("LineChartView.fxml"));
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
			reverse(closePrice);
			reverse(dateStrings);
		}
		catch(FileNotFoundException e){
			e.getStackTrace();
		}

		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
		for (int i = 0; i < closePrice.size();i++) {
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
			reverse(closePrice);
			reverse(dateStrings);
		}
		catch(FileNotFoundException e){
			e.getStackTrace();
		}

		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
		for (int i = closePrice.size() - range; i < closePrice.size();i++) {
			series.getData().add(new XYChart.Data<String, Number>(dateStrings.get(i), closePrice.get(i)));
		}
		return series;
	}


	public static void main(String[] args) {

		launch(args);
	}
}

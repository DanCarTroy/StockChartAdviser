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
	XYChart.Series<String,Number> currentDataSeries = new XYChart.Series<String, Number>();

	public MainApp(){
		currentDataSeries = getCurrentSeries();
	}
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
		Scanner csvFile;
		DecimalFormat f = new DecimalFormat("###.##");
		ArrayList<Double> closePrice = new ArrayList<>();
		ArrayList<String> dateStrings = new ArrayList<>();
		int dateStringIndex = 0;
		int closePriceIndex = 4;
		try{
			csvFile = new Scanner(new File("src/mastero/opto/view/Sample data.csv"));//aaa
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
		for (int i = range - 1; i > -1;i--) {
			series.getData().add(new XYChart.Data<String, Number>(dateStrings.get(i), closePrice.get(i)));
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

	public static void main(String[] args) {
		launch(args);
	}
}

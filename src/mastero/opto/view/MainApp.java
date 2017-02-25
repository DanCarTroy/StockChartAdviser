package mastero.opto.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
	private Stage primaryStage = new Stage();
	private static BorderPane mainLayout = new BorderPane();

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ProfitUs");
		showMainWindow();
	}

	public void showMainWindow()throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("MainWindow.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public static void showChartView() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("LineChartView.fxml"));
		BorderPane chartView = loader.load();
		mainLayout.setCenter(chartView);
	}


	public static void main(String[] args) {
		launch(args);
	}
}

package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane mainLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("ProfitUs");
		showMainView();
	}

	public void showMainView()throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("MainWindow.fxml"));
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void showChartWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("Main/LineChart/LineChartView.fxml"));
		Scene scene = loader.load();
	}


	public static void main(String[] args) {
		launch(args);
	}
}

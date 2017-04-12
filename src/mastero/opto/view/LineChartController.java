package mastero.opto.view;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mastero.opto.MainApp;
import mastero.opto.model.Signal;
import mastero.opto.model.Stock;
import mastero.opto.util.StockDownloader;

import java.io.IOException;

/**
 * Created by LucienChu on 2017-02-25.
 */
public class LineChartController {

	@FXML
    private TableView<Stock> stockTable;
    @FXML
    private TableColumn<Stock, String> stockSymbolColumn;
    @FXML
    private TableColumn<Stock, String> stockNameColumn;

    @FXML
    private TableView<Stock> mostActiveTable;
    @FXML
    private TableColumn<Stock, String> activeSymbolColumn;
    @FXML
    private TableColumn<Stock, String> activeNameColumn;

    @FXML
    private TableView<Stock> dow30Table;
    @FXML
    private TableColumn<Stock, String> d30SymbolColumn;
    @FXML
    private TableColumn<Stock, String> d30NameColumn;

    @FXML
    private TableView<Signal> signalTable;
    @FXML
    private TableColumn<Signal, String> sigDateColumn;
    @FXML
    private TableColumn<Signal, String> sigActionColumn;

    private ObservableList<Signal> signalList = FXCollections.observableArrayList();

    private Stock currentStock;

    MainApp main = new MainApp();
    private static final int ONE_YEAR = 365;
    private static final int TWO_YEAR = 730;
    private static final int FIVE_YEAR = 1825;
    XYChart.Series<String, Number> sma20 = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> sma50 = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> sma100 = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> sma200 = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
    @FXML Button allDataButton;
    @FXML Button oneYearDataButton;
    @FXML Button twoYearDataButton;
    @FXML Button fiveYearDataDataButton;
    @FXML Button smaButton;
    @FXML CheckBox sma_20;
    @FXML CheckBox sma_50;
    @FXML CheckBox sma_100;
    @FXML CheckBox sma_200;
    @FXML Button cutDataBtn;
    @FXML LineChart<String, Number>  lineChart;
    @FXML
    Label label;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public LineChartController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    	try{
        // Initialize the favorite stock table with the two columns.
    	stockSymbolColumn.setCellValueFactory(cellData -> cellData.getValue().stockSymbolProperty());
    	stockNameColumn.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());


    	// Listen for selection changes and pass the selected stock so that it can be used in the program.
        stockTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setCurrentStock(newValue));

     // Initialize the Dow30 stock table with the two columns.
    	d30SymbolColumn.setCellValueFactory(cellData -> cellData.getValue().stockSymbolProperty());
    	d30NameColumn.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());


    	// Listen for selection changes and pass the selected stock so that it can be used in the program.
        dow30Table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setCurrentStock(newValue));

     // Initialize the Most Active stock table with the two columns.
    	activeSymbolColumn.setCellValueFactory(cellData -> cellData.getValue().stockSymbolProperty());
    	activeNameColumn.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());


    	// Listen for selection changes and pass the selected stock so that it can be used in the program.
        mostActiveTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setCurrentStock(newValue));

     // Initialize the Signal table with the two columns.
    	sigDateColumn.setCellValueFactory(cellData -> cellData.getValue().signalDateProperty());
    	sigActionColumn.setCellValueFactory(cellData -> cellData.getValue().signalNameProperty());

    	}
    	catch(NullPointerException e)
    	{
    		System.out.println("NASDAQ, CNN, or Yahoo did not cooperate :(");
    	}
    }

    public void setCurrentStock(Stock stk){
    	currentStock = stk;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        main = mainApp;

        // Add observable list data to the table
        stockTable.setItems(mainApp.getStockList());
        dow30Table.setItems(mainApp.getDow30List());
        mostActiveTable.setItems(mainApp.getMostActiveList());
    }

    @FXML
    private void showMainFrame() throws IOException{
        main.showMainWindow();
    }

    public void allDatabtn()throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol());
        if (StockDownloader.getChartData(currentStock.getStockSymbol()).getData().size() == 0)
            showErroPopUp();
        else {
            data.setName("All data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
        }
    }

    public void oneYearDatabtn() throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol(), ONE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
            data.setName("1 years data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
        }
    }

    public void twoYearDatabtn(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol(), TWO_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
            data.setName("2 years data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
        }
    }

    public void fiveYearDatabtn(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol(), FIVE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
            data.setName("5 years data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
        }
    }

    public void showErroPopUp()throws IOException{
        Stage stage;
        Parent root;
        stage = new Stage();
        root = FXMLLoader.load(getClass().getResource("errorPopUp.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void getSMA20(){
        DataAnalysis analysis = new DataAnalysis(data);
        sma20 = analysis.SMA20();
        sma20.setName("SMA 20");
    }


    public void getSMA50 (){
        DataAnalysis analysis = new DataAnalysis(data);
        sma50 = analysis.SMA50();
        sma50.setName("SMA 50");
    }


    public void getSMA100(){
        DataAnalysis analysis = new DataAnalysis(data);
        sma100 = analysis.SMA100();
        sma100.setName("SMA 100");
    }

    public void getSMA200(){
        DataAnalysis analysis = new DataAnalysis(data);
        sma200 = analysis.SMA200();
        sma200.setName("SMA 200");
    }


    public void handle20Checkboxes(){

        if(sma_20.isSelected())
            lineChart.getData().add(sma20);
        else
            lineChart.getData().remove(sma20);
    }

    public void handle50Checkboxes(){
        if(sma_50.isSelected())
            lineChart.getData().add(sma50);
        else
            lineChart.getData().remove(sma50);
    }

    public void handle100Checkboxes(){
        if(sma_100.isSelected())
            lineChart.getData().add(sma100);
        else
            lineChart.getData().remove(sma100);
    }

    public void handle200Checkboxes(){
        if(sma_200.isSelected())
            lineChart.getData().add(sma200);
        else
            lineChart.getData().remove(sma200);
    }

    public void oneYearNiceChartbtn() throws IOException{
        lineChart.getData().clear();
        signalTable.getItems().clear();

        data = StockDownloader.getChartData(currentStock.getStockSymbol(), ONE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
            data.setName("1 years data");
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
            sma20.setName("20 days SMA");
            sma50.setName("50 days SMA");
            sma100.setName("100 days SMA");
            sma200.setName("200 days SMA");
            if (sma200.getData().size() != 0) {
                data = cutData(data, 200);
                sma20 = cutData(sma20, 200 - 20);
                sma50 = cutData(sma50, 200 - 50);
                sma100 = cutData(sma100, 200 - 100);
                lineChart.getData().add(sma200);
                lineChart.getData().add(sma100);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            } else if (sma100.getData().size() != 0) {
                data = cutData(data, 100);
                sma20 = cutData(sma20, 100 - 20);
                sma50 = cutData(sma50, 100 - 50);
                lineChart.getData().add(sma100);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            } else if (sma50.getData().size() != 0) {
                data = cutData(data, 50);
                sma20 = cutData(sma20, 50 - 20);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            } else if (sma20.getData().size() != 0) {
                data = cutData(data, 20);
                lineChart.getData().add(sma20);
            }
            lineChart.getData().add(data);

            DataAnalysis analysis = new DataAnalysis(data);
            XYChart.Series<String, Number> buySignal = analysis.getBuySignals();
            buySignal.setName("buy signal");


            XYChart.Series<String, Number> sellSignal = analysis.getSellSignals();
            sellSignal.setName("sell signal");
            lineChart.getData().add(buySignal);

            if(buySignal != null && buySignal.getData().size() != 0){
                for(int i = 0; i < buySignal.getData().size(); i++){

                    String string = buySignal.getData().get(i).getXValue();
                    String action = "buy";

                    System.out.println("buy signal "+i+": "+string);
                    //put string and action on a table please
                    signalList.add(new Signal(string,action));
                }
            }
            //do exactly the same thing for sell Signal.
            if(sellSignal != null && sellSignal.getData().size() != 0){
                for(int i = 0; i < sellSignal.getData().size(); i++){

                    String string = sellSignal.getData().get(i).getXValue();
                    String action = "sell";

                    System.out.println("sell signal "+i+": "+string);
                    //put string and action on a table please
                    signalList.add(new Signal(string,action));

                }
            }

         // Populate the Signal table with the list.
            signalTable.setItems(signalList);
        	//sigDateColumn.setCellValueFactory(cellData -> cellData.getValue().signalDateProperty());
        	//sigActionColumn.setCellValueFactory(cellData -> cellData.getValue().signalNameProperty());
        }
    }

    public XYChart.Series<String, Number> cutData(XYChart.Series<String, Number> series ,int stop){
        if (series.getData().size() > stop){
            series.getData().remove(0, stop);
        }
        return series;
    }
}

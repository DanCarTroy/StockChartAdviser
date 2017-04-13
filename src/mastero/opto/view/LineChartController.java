package mastero.opto.view;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;



import javafx.embed.swing.SwingFXUtils;

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
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mastero.opto.MainApp;
import mastero.opto.model.Signal;
import mastero.opto.model.Stock;
import mastero.opto.util.StockDownloader;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

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
    XYChart.Series<String, Number> buySignals = new XYChart.Series<String, Number>();
    XYChart.Series<String, Number> sellSignals = new XYChart.Series<String, Number>();
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

    /*
    private boolean allDataButtonFlag = false;
    private boolean oYDButtonFlag = false;
    private boolean tYDButtonFlag = false;
    private boolean fYDButtonFlag = false;
    //private boolean smaButtonFlag = false;
    private boolean sma_20CBoxFlag = false;
    private boolean sma_50CBoxFlag = false;
    private boolean sma_100CBoxFlag = false;
    private boolean sma_200CBoxFlag = false; */

    private enum IndicatorToReload {none, all, one, two, five,
    	sma20, sma50, sma100, sma200};

    private IndicatorToReload  flagToReload = IndicatorToReload.none;

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

    	try {
			oneYearAction();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	handle20Checkboxes();
        handle50Checkboxes();
        handle100Checkboxes();
        handle200Checkboxes();

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
            DataAnalysis analysis = new DataAnalysis(data);
            data.setName("All data");
            lineChart.getData().add(data);
            sma20 = analysis.getSMA20();
            sma50 = analysis.getSMA50();
            sma100 = analysis.getSMA100();
            sma200 = analysis.getSMA200();
            buySignals = analysis.getBuySignals();
            sellSignals = analysis.getSellSignals();
            sma20.setName("20 days SMA");
            sma50.setName("50 days SMA");
            sma100.setName("100 days SMA");
            sma200.setName("200 days SMA");
        }

        flagToReload = IndicatorToReload.all;
    }

    public void oneYearDatabtn() throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol(), ONE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
            //DataAnalysis analysis = new DataAnalysis(data);
            data.setName("1 years data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
            //buySignals = analysis.getBuySignals();
            //sellSignals = analysis.getSellSignals();
            //sma20.setName("20 days SMA");
            //sma50.setName("50 days SMA");
            //sma100.setName("100 days SMA");
            //sma200.setName("200 days SMA");
        }

      //Making things more automatic for the user
        	handle20Checkboxes();
            handle50Checkboxes();
            handle100Checkboxes();
            handle200Checkboxes();


        flagToReload = IndicatorToReload.one;
    }

    public void twoYearDatabtn(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol(), TWO_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
        	//DataAnalysis analysis = new DataAnalysis(data);
            data.setName("2 years data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
            //buySignals = analysis.getBuySignals();
            //sellSignals = analysis.getSellSignals();
        }

      //Making things more automatic for the user
        	handle20Checkboxes();
            handle50Checkboxes();
            handle100Checkboxes();
            handle200Checkboxes();


        flagToReload = IndicatorToReload.two;
    }

    public void fiveYearDatabtn(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        data = StockDownloader.getChartData(currentStock.getStockSymbol(), FIVE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
        	//DataAnalysis analysis = new DataAnalysis(data);
            data.setName("5 years data");
            lineChart.getData().add(data);
            getSMA20();
            getSMA50();
            getSMA100();
            getSMA200();
            //buySignals = analysis.getBuySignals();
            //sellSignals = analysis.getSellSignals();
        }
        	//Making things more automatic for the user

        	handle20Checkboxes();
            handle50Checkboxes();
            handle100Checkboxes();
            handle200Checkboxes();


        flagToReload = IndicatorToReload.five;
    }


    public void oneYearAction() throws IOException{
        lineChart.getData().clear();
        //lineChart.setCreateSymbols(false);
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

        flagToReload = IndicatorToReload.one;
    }

    public void twoYearAction(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        //lineChart.setCreateSymbols(false);
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

        flagToReload = IndicatorToReload.two;
    }

    public void fiveYearAction(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        //lineChart.setCreateSymbols(false);
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

        flagToReload = IndicatorToReload.five;
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

        /**
     * Saves a screenshot of current chart to the specified file.
     *
     * @param file
     */
    public void saveScreenshotToFile(File file) {
        try {

        	//defining the axes
            final CategoryAxis x = new CategoryAxis();
            final NumberAxis y = new NumberAxis();
            x.setLabel("Date");
            y.setLabel("Close Price");
        	LineChart<String, Number>  lchart = new LineChart<String, Number>(x, y); //lineChart;
        	lchart.setAnimated(false);
        	lineChart.setAnimated(false);


        	lineChart.getData().remove(data);
            lineChart.getData().remove(sma20);
            lineChart.getData().remove(sma50);
            lineChart.getData().remove(sma100);
            lineChart.getData().remove(sma200);


        	lchart.getData().add(data);
        	if(sma_20.isSelected() && sma20 != null)
        		lchart.getData().add(sma20);
        	if(sma_50.isSelected() && sma50 != null)
        		lchart.getData().add(sma50);
        	if(sma_100.isSelected() && sma100 != null)
        		lchart.getData().add(sma100);
        	if(sma_200.isSelected() && sma200 != null)
        		lchart.getData().add(sma200);


        	/*
        	FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("view/LineChartView.fxml"));
    		AnchorPane newchartView = loader.load();
    		main.getMainLayout().setCenter(newchartView);
			*/

        	Scene scene = new Scene(lchart, 800, 600);
        	saveAsPng(scene, file.getPath());
            //stage.setScene(scene);
            //saveAsPng(scene, "c:\\temp\\chart1.png");

            // Save the file path to the registry.
            setUserFilePath(file);


        	sma20.getData().clear();// = new XYChart.Series<String, Number>();
            sma50.getData().clear();// = new XYChart.Series<String, Number>();
            sma100.getData().clear();// = new XYChart.Series<String, Number>();
            sma200.getData().clear();// = new XYChart.Series<String, Number>();
            data.getData().clear();// = new XYChart.Series<String, Number>();



            ActionEvent event = new ActionEvent();
            switch(flagToReload)
            {
	            case all: System.out.println("Im all");allDatabtn(); break;
	            case one: System.out.println("Im one");oneYearAction(); break;
	            case two: System.out.println("Im two");twoYearAction(event); break;
	            case five:System.out.println("Im five");fiveYearAction(event); break;
	            default: flagToReload = IndicatorToReload.none; main.showChartView(); break;
            }

            handle20Checkboxes();
            handle50Checkboxes();
            handle100Checkboxes();
            handle200Checkboxes();


            //main.showChartView();
        } catch (Exception e) { // catches ANY exception
        	e.printStackTrace();
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Could not save data");
        	alert.setContentText("Could not save data to file:\n" + file.getPath());

        	alert.showAndWait();
        }
    }

    public void saveAsPng(Scene scene, String path) {
    	WritableImage image = scene.snapshot(null);
        File file = new File(path);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setUserFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            main.getPrimaryStage().setTitle("StockChartAdviser - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            main.getPrimaryStage().setTitle("StockChartAdviser");
        }
    }

    /**
     * Saves a screenshot to the location that was previously used to save. If there is no
     * previous location, the "save as" dialog is shown.
     */
    public void saveImage() throws IOException{
        File file = main.getLastOpenedFile();
        if (file != null) {
            main.saveScreenshot(file);
        } else {
            saveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    private void saveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".png")) {
                file = new File(file.getPath() + ".png");
            }
            main.saveScreenshot(file);
        }
    }

    public void logout()
    {
    	main.initLogin();
    }


    public void oneYearNiceChartbtn() throws IOException{
    	/*sma20.getData().clear();
        sma50.getData().clear();
        sma100.getData().clear();
        sma200.getData().clear();
        data.getData().clear();*/

    	lineChart.getData().clear();
        signalTable.getItems().clear();


        data = StockDownloader.getChartData(currentStock.getStockSymbol(), ONE_YEAR);
        int dataSize = data.getData().size();
        if (data.getData().size() == 0)
            showErroPopUp();
        else {
            data.setName("1 years data");
            DataAnalysis analysis = new DataAnalysis(data);
            sma20 = analysis.getSMA20();
            int sma20Size = sma20.getData().size();
            sma50 = analysis.getSMA50();
            int sma50Size = sma50.getData().size();
            sma100 = analysis.getSMA100();
            int sma100Size = sma100.getData().size();
            sma200 = analysis.getSMA200();
            int sma200Size= sma200.getData().size();
            buySignals = analysis.getBuySignals();
            sellSignals = analysis.getSellSignals();
            sma20.setName("20 days SMA");
            sma50.setName("50 days SMA");
            sma100.setName("100 days SMA");
            sma200.setName("200 days SMA");
            if (sma200.getData().size() != 0) {
                data = cutData(data, dataSize - sma200Size);
                sma20 = cutData(sma20, sma20Size - sma200Size);
                sma50 = cutData(sma50, sma50Size - sma200Size);
                sma100 = cutData(sma100, sma100Size - sma200Size);
                lineChart.getData().add(sma200);
                lineChart.getData().add(sma100);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            } else if (sma100.getData().size() != 0) {
                data = cutData(data, dataSize - sma100Size);
                sma20 = cutData(sma20, sma20Size - sma100Size);
                sma50 = cutData(sma50, sma50Size - sma100Size);
                lineChart.getData().add(sma100);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            } else if (sma50.getData().size() != 0) {
                data = cutData(data, dataSize - sma50Size);
                sma20 = cutData(sma20, sma20Size - sma50Size);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            } else if (sma20.getData().size() != 0) {
                data = cutData(data, dataSize - sma20Size);
                lineChart.getData().add(sma20);
            }
            lineChart.getData().add(data);
            buySignals = removeData(data, buySignals);
            buySignals.setName("buy signal");
            sellSignals = removeData(data, sellSignals);
            sellSignals.setName("sell signal");
            lineChart.getData().add(buySignals);
            lineChart.getData().add(sellSignals);

            if(buySignals != null && buySignals.getData().size() != 0){
                for(int i = 0; i < buySignals.getData().size(); i++){

                    String string = buySignals.getData().get(i).getXValue();
                    String action = "buy";

                    System.out.println("buy signal "+i+": "+string);
                    //put string and action on a table please
                    signalList.add(new Signal(string,action));
                }
            }
            //do exactly the same thing for sell Signal.
            if(sellSignals != null && sellSignals.getData().size() != 0){
                for(int i = 0; i < sellSignals.getData().size(); i++){

                    String string = sellSignals.getData().get(i).getXValue();
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
    public XYChart.Series<String, Number> removeData(XYChart.Series<String, Number> data0, XYChart.Series<String, Number> data1){
        XYChart.Series<String, Number> temp0 = copy(data1);
        if(data0 != null && data1 != null & data0.getData().size() > 0 && data1.getData().size() > 0){
            String target = data0.getData().get(0).getXValue();
            int index = data1.getData().size();
            for (int i = 0; i < index; i++){
                String walk = temp0.getData().get(i).getXValue();
                if(walk.compareTo(target) < 0) {
                    temp0.getData().remove(i);
                    index = temp0.getData().size();
                    i--;
                }
            }
        }
        return temp0;
    }

    public XYChart.Series<String, Number> copy(XYChart.Series<String, Number> target){
        XYChart.Series<String, Number> temp = new XYChart.Series<>();
        for(int i = 0; i < target.getData().size(); i++){
            String string = target.getData().get(i).getXValue();
            Number number = target.getData().get(i).getYValue();
            temp.getData().add(new XYChart.Data<>(string, number));

        }
        return temp;
}

}

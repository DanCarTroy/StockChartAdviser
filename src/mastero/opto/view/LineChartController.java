package mastero.opto.view;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mastero.opto.MainApp;
import java.io.IOException;

/**
 * Created by LucienChu on 2017-02-25.
 */
public class LineChartController {
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
    private void showMainFrame() throws IOException{
        main.showMainWindow();
    }

    public void allDatabtn()throws IOException{
        lineChart.getData().clear();
        lineChart.setCreateSymbols(false);
        data = main.getChartData();
        if (main.getChartData().getData().size() == 0)
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
        lineChart.setCreateSymbols(false);
        data = main.getChartData(ONE_YEAR);
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
        lineChart.setCreateSymbols(false);
        data = main.getChartData(TWO_YEAR);
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
        lineChart.setCreateSymbols(false);
        data = main.getChartData(FIVE_YEAR);
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
        lineChart.setCreateSymbols(false);
        DataAnalysis analysis = new DataAnalysis(data);
        sma20 = analysis.SMA20();
        sma20.setName("SMA 20");
    }


    public void getSMA50 (){
        lineChart.setCreateSymbols(false);
        DataAnalysis analysis = new DataAnalysis(data);
        sma50 = analysis.SMA50();
        sma50.setName("SMA 50");
    }


    public void getSMA100(){
        lineChart.setCreateSymbols(false);
        DataAnalysis analysis = new DataAnalysis(data);
        sma100 = analysis.SMA100();
        sma100.setName("SMA 100");
    }

    public void getSMA200(){
        lineChart.setCreateSymbols(false);
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
        lineChart.setCreateSymbols(false);
        data = main.getChartData(ONE_YEAR);
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
            if(sma200.getData().size() != 0){
                data = cutData(data,200);
                sma20 = cutData(sma20, 200-20);
                sma50 = cutData(sma50,200-50);
                sma100 = cutData(sma100,200-100);
                lineChart.getData().add(sma200);
                lineChart.getData().add(sma100);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            }

            else if(sma100.getData().size() != 0){
                data = cutData(data,100);
                sma20 = cutData(sma20, 100-20);
                sma50 = cutData(sma50,100-50);
                lineChart.getData().add(sma100);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            }
            else if(sma50.getData().size() != 0){
                data = cutData(data,50);
                sma20 = cutData(sma20, 50-20);
                lineChart.getData().add(sma50);
                lineChart.getData().add(sma20);
            }

            else if(sma20.getData().size() != 0){
                data = cutData(data,20);
                lineChart.getData().add(sma20);
            }
            lineChart.getData().add(data);
        }
    }

    public XYChart.Series<String, Number> cutData(XYChart.Series<String, Number> series ,int stop){
        if (series.getData().size() > stop){
            series.getData().remove(0, stop);
        }
        return series;
    }
}

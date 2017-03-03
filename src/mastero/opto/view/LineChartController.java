package mastero.opto.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mastero.opto.view.MainApp;
import java.io.IOException;

/**
 * Created by LucienChu on 2017-02-25.
 */
public class LineChartController {
    MainApp main = new MainApp();
    private static final int ONE_YEAR = 365;
    private static final int TWO_YEAR = 730;
    private static final int FIVE_YEAR = 1825;
    XYChart.Series<String, Number> sma = new XYChart.Series<String, Number>();
    @FXML Button allDataButton;
    @FXML Button oneYearDataButton;
    @FXML Button twoYearDataButton;
    @FXML Button fiveYearDataDataButton;
    @FXML Button smaButton;
    @FXML LineChart<String, Number>  lineChart;
    @FXML
    private void showMainFrame() throws IOException{
        main.showMainWindow();
    }

    public void allDatabtn()throws IOException{
        XYChart.Series data = main.getChartData();
        if (main.getChartData().getData().size() == 0)
            showErroPopUp();
        else {
            lineChart.getData().clear();
            lineChart.getData().add(data);
        }
    }

    public void oneYearDatabtn() throws IOException{
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData(ONE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(data);
    }

    public void twoYearDatabtn(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData(TWO_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(data);
    }

    public void fiveYearDatabtn(ActionEvent event) throws IOException{
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData(FIVE_YEAR);
        if (data.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(data);
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

    public void showSMA20(ActionEvent event) throws IOException{
        lineChart.getData().removeAll(sma);
        DataAnalysis analysis = new DataAnalysis(main.getCurrentSeries());
        sma = analysis.SMA20();
        if (sma.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(sma);
    }

    public void showSMA50(ActionEvent event) throws IOException{
        lineChart.getData().removeAll(sma);
        DataAnalysis analysis = new DataAnalysis(main.getCurrentSeries());
        sma = analysis.SMA50();
        if (sma.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(sma);
    }

    public void showSMA100(ActionEvent event) throws IOException{
        lineChart.getData().removeAll(sma);
        DataAnalysis analysis = new DataAnalysis(main.getCurrentSeries());
        sma = analysis.SMA100();
        if (sma.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(sma);
    }

    public void showSMA200(ActionEvent event) throws IOException{
        lineChart.getData().removeAll(sma);
        DataAnalysis analysis = new DataAnalysis(main.getCurrentSeries());
        sma = analysis.SMA200();
        if (sma.getData().size() == 0)
            showErroPopUp();
        else
            lineChart.getData().add(sma);
    }
}

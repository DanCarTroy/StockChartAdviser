package mastero.opto.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import mastero.opto.view.MainApp;
import java.io.IOException;

/**
 * Created by LucienChu on 2017-02-25.
 */
public class LineChartController {
    private MainApp main = new MainApp();
    private static final int ONE_YEAR = 365;
    private static final int TWO_YEAR = 730;
    private static final int FIVE_YEAR = 1825;

    @FXML
    LineChart<String, Number>  lineChart;
    @FXML
    private void showMainFrame() throws IOException{
        main.showMainWindow();
    }

    public void allDatabtn(ActionEvent event){
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData();
        lineChart.getData().add(data);
    }

    public void oneYearDatabtn(ActionEvent event) {
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData(ONE_YEAR);
        lineChart.getData().add(data);
    }

    public void twoYearDatabtn(ActionEvent event) {
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData(TWO_YEAR);
        lineChart.getData().add(data);
    }

    public void fiveYearDatabtn(ActionEvent event) {
        lineChart.getData().clear();
        XYChart.Series data = main.getChartData(FIVE_YEAR);
        lineChart.getData().add(data);
    }
}

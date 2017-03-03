package mastero.opto.view;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;

/**
 * Created by LucienChu on 2017-03-03.
 */
public class DataAnalysis {
    MainApp main = new MainApp();
    XYChart.Series inData = main.getCurrentSeries();
    ArrayList<String> dateStrings = new ArrayList<>();
    ArrayList<Number> price = new ArrayList<>();

    public DataAnalysis(){
        int size = inData.getData().size();
        for(int i = 0; i < size; i++){
            XYChart.Data data = (XYChart.Data)inData.getData().get(i);
            dateStrings.add((String) data.getXValue());
            price.add((Number) data.getYValue());
        }
    }

    public ArrayList<Number> SMA(int walk) {
        ArrayList<Number> temp0 = new ArrayList<>();
        ArrayList<String> temp1 = new ArrayList<>();
        int initialPosition = walk / 2;
        for (int i = 0; i + walk <= price.size(); i++) {
            int j = i + walk - 1;
            float smaPrice = sum(i, j) / walk;
            int dataPosition = initialPosition + i;
            HistoricalData inData = this.inData.get(dataPosition);
            HistoricalData outData = inData.copy();
            outData.setClose(smaPrice);
            temp.add(outData);
        }
        outData = temp;
        return outData;
    }
    public float sum(int start, int end) {
        float total = 0;
        for (int i = start; i < end + 1; i++) {
            double singleClosePrice = (double) price.get(i);
            total += singleClosePrice;
        }
        return total;
    }

}

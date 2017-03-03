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
    public static final int TWENTY = 20;
    public static final int FIFTY = 50;
    public static final int HUNDRED = 100;
    public static final int TWO_HUNDRED = 200;

    public DataAnalysis(){
        int size = inData.getData().size();
        for(int i = 0; i < size; i++){
            XYChart.Data data = (XYChart.Data)inData.getData().get(i);
            dateStrings.add((String) data.getXValue());
            price.add((Number) data.getYValue());
        }
    }

    public XYChart.Series<String, Number> SMA(int walk) {
        ArrayList<String> temp0 = new ArrayList<>();
        ArrayList<Number> temp1 = new ArrayList<>();
        int initialPosition = walk / 2 + 1;
        for (int i = 0; i + walk <= price.size(); i++) {
            int j = i + walk - 1;
            float smaPrice = sum(i, j) / walk;
            temp1.add(smaPrice);
            temp0.add(dateStrings.get(initialPosition));
        }
        XYChart.Series<String, Number> temp = new XYChart.Series<String, Number>();
        for(int i = 0; i < temp0.size(); i++)
            temp.getData().add(new XYChart.Data<String, Number>(temp0.get(i), temp1.get(i)));
        return temp;
    }
    public float sum(int start, int end) {
        float total = 0;
        for (int i = start; i < end + 1; i++) {
            double singleClosePrice = (double) price.get(i);
            total += singleClosePrice;
        }
        return total;
    }
    public XYChart.Series<String, Number> SMA20(int walk) {
        return SMA(TWENTY);
    }
    public XYChart.Series<String, Number> SMA50(int walk) {
        return SMA(FIFTY);
    }
    public XYChart.Series<String, Number> SMA100(int walk) {
        return SMA(HUNDRED);
    }
    public XYChart.Series<String, Number> SMA200(int walk) {
        return SMA(TWO_HUNDRED);
    }

}

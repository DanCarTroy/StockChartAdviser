package mastero.opto.view;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;

/**
 * Created by LucienChu on 2017-03-03.
 */
public class DataAnalysis {
    ArrayList<String> dateStrings = new ArrayList<>();
    ArrayList<Number> price = new ArrayList<>();
    XYChart.Series<String, Number> data = null;
    XYChart.Series<String, Number> buySignals = new XYChart.Series<>();
    XYChart.Series<String, Number> sellSignals = new XYChart.Series<>();
    public static final int TWENTY = 20;
    public static final int FIFTY = 50;
    public static final int HUNDRED = 100;
    public static final int TWO_HUNDRED = 200;

    public DataAnalysis(XYChart.Series data){
        int size = data.getData().size();
        XYChart.Series<String, Number> temp0 = new XYChart.Series<>();
        for(int i = 0; i < size; i++){
            XYChart.Data aData = (XYChart.Data)data.getData().get(i);
            String string = (String) aData.getXValue();
            dateStrings.add(string);
            Number number = (Number) aData.getYValue();
            price.add(number);

            XYChart.Data temp1 = new XYChart.Data(string, number);
            temp0.getData().add(temp1);
        }
        this.data = temp0;
        analysizeAllSMAs();
    }

    public XYChart.Series<String, Number> SMA(int walk) {
        ArrayList<String> temp0 = new ArrayList<>();
        ArrayList<Number> temp1 = new ArrayList<>();
        int initialPosition = walk;
        for (int i = 0; i < price.size() - walk ; i++) {
            int j = i + walk - 1;
            double smaPrice = sum(i, j) / walk;
            temp1.add(smaPrice);
            temp0.add(dateStrings.get(initialPosition));
            initialPosition++;
        }
        XYChart.Series<String, Number> temp = new XYChart.Series<String, Number>();
        for(int i = 0; i < temp0.size(); i++)
            temp.getData().add(new XYChart.Data<String, Number>(temp0.get(i), temp1.get(i)));
        return temp;
    }
    public double sum(int start, int end) {
        double total = 0;
        for (int i = start; i < end + 1; i++) {
            double singleClosePrice = (double) price.get(i);
            total += singleClosePrice;
        }
        return total;
    }
    public XYChart.Series<String, Number> SMA20() {
        return SMA(TWENTY);
    }
    public XYChart.Series<String, Number> SMA50() {
        return SMA(FIFTY);
    }
    public XYChart.Series<String, Number> SMA100() {
        return SMA(HUNDRED);
    }
    public XYChart.Series<String, Number> SMA200() {
        return SMA(TWO_HUNDRED);
    }


    public void singleSMAAnalysis(XYChart.Series<String, Number> sma){
        if (data == null || data.getData().size() == 0 || sma == null || sma.getData().size() < 2){
            buySignals = null;
            sellSignals = null;
        }

        else{
            int position0 = data.getData().size() - sma.getData().size();

            for(int i = 0; i < sma.getData().size() - 1; i++){
                XYChart.Data p0 = data.getData().get(position0 + i);
                XYChart.Data p1 = data.getData().get(position0 +  i + 1);
                XYChart.Data c0 = sma.getData().get(i);
                XYChart.Data c1 = sma.getData().get(i + 1);
                if (crossAbove(p0,p1,c0,c1)) {
                    XYChart.Data<String, Number> temp = new XYChart.Data<String, Number>((String)c1.getXValue(),150);
                    buySignals.getData().add(temp);
                }

                else if (crossBelow(p0,p1,c0,c1)) {
                    XYChart.Data<String, Number> temp = new XYChart.Data<String, Number>((String)c1.getXValue(),150);
                    sellSignals.getData().add(temp);
                }
            }
        }
    }

    public void analysizeAllSMAs(){
        XYChart.Series<String, Number> sma20 = SMA20();
        XYChart.Series<String, Number> sma50 = SMA50();
        XYChart.Series<String, Number> sma100 = SMA100();
        XYChart.Series<String, Number> sma200 = SMA200();
        if (sma20 != null && sma20.getData().size() != 0)
            this.singleSMAAnalysis(sma20);
        if (sma50 != null && sma50.getData().size() != 0)
            this.singleSMAAnalysis(sma50);
        if (sma100 != null && sma100.getData().size() != 0)
            this.singleSMAAnalysis(sma100);
        if (sma200 != null && sma200.getData().size() != 0)
            this.singleSMAAnalysis(sma200);
    }
    public boolean cross(XYChart.Data p0, XYChart.Data p1, XYChart.Data c0, XYChart.Data c1){
        double precision = (double) 0.00000000000000001;
        double preDiff = (double) c0.getYValue() - (double)p0.getYValue();
        double curDiff = (double) c1.getYValue() - (double) p1.getYValue();
        double product = preDiff*curDiff;
        return (product < 0 || product < precision);
    }
    public boolean crossAbove(XYChart.Data p0, XYChart.Data p1, XYChart.Data c0, XYChart.Data c1){
        double diff = (double) p1.getYValue() - (double) c1.getYValue();
        return this.cross(p0,p1,c0,c1) && diff > 0;
    }

    public boolean crossBelow(XYChart.Data p0, XYChart.Data p1, XYChart.Data c0, XYChart.Data c1){
        double diff = (double) p1.getYValue() - (double) c1.getYValue();
        return this.cross(p0,p1,c0,c1) && diff < 0;
    }

    public boolean rising(XYChart.Data  prevData, XYChart.Data  postData){
        double diff = (double) postData.getYValue() - (double) prevData.getYValue();
        return (diff > 0);
    }

    public boolean descending(XYChart.Data prevData, XYChart.Data postData){
        double diff = (double) postData.getYValue() - (double) prevData.getYValue();
        return (diff < 0);
    }

    public XYChart.Series<String, Number> getSellSignals(){
        if(sellSignals == null || sellSignals.getData().size() == 0)
            return null;
        else{
            XYChart.Series<String, Number> temp = new XYChart.Series<>();
            for(int i = 0; i < sellSignals.getData().size(); i++){
                String string = sellSignals.getData().get(i).getXValue();
                Number number = sellSignals.getData().get(i).getYValue();
                temp.getData().add(new XYChart.Data<>(string, number));
            }
            return temp;
        }
    }

    public XYChart.Series<String, Number> getBuySignals(){
        if(buySignals.getData().size() == 0)
            return null;
        else{
            XYChart.Series<String, Number> temp = new XYChart.Series<>();
            for(int i = 0; i < buySignals.getData().size(); i++){
                String string = buySignals.getData().get(i).getXValue();
                Number number = buySignals.getData().get(i).getYValue();
                temp.getData().add(new XYChart.Data<>(string, number));
            }
            return temp;
        }
    }
}

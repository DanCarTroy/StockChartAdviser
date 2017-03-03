package mastero.opto.view;

import javafx.scene.chart.XYChart;

/**
 * Created by LucienChu on 2017-03-03.
 */
public class test {
    public static void main(String[] args) {
        MainApp main = new MainApp();
        XYChart.Series<String,Number> s1 = new XYChart.Series<String, Number>();
        s1.getData().add(new XYChart.Data<>("A", 100));
        s1.getData().add(new XYChart.Data<>("B", 200));
        s1.getData().add(new XYChart.Data<>("C", 300));
        s1.getData().add(new XYChart.Data<>("D", 400));
        s1.getData().add(new XYChart.Data<>("E", 500));
        s1.getData().add(new XYChart.Data<>("F", 600));

        XYChart.Series<String,Number> s2 = main.copySeries(s1);
        for(int i = 0; i < s1.getData().size(); i++){
           XYChart.Data data = s2.getData().get(i);
           System.out.println(data.toString());
        }
    }
}

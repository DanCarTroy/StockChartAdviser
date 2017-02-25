package mastero.opto.view;

import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Created by LucienChu on 2017-02-24.
 */
public class MainAppController {
    public MainApp main = new MainApp();

    @FXML
    public void goToLineChartWindow()throws IOException{
        main.showChartView();
    }
}

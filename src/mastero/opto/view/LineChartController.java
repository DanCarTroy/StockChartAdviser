package mastero.opto.view;

import javafx.fxml.FXML;
import mastero.opto.view.MainApp;

import java.io.IOException;

/**
 * Created by LucienChu on 2017-02-25.
 */
public class LineChartController {
    private MainApp main = new MainApp();

    @FXML
    private void showMainFrame() throws IOException{
        main.showMainWindow();
    }
}

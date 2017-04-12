package mastero.opto.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.XYChart;

/**
 * Model class for a Signal.
 *
 */
public class Signal {

    private final StringProperty signalDate;
    private final StringProperty signalName;

    /**
     * Default constructor.
     */
    public Signal() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param signalDate
     * @param signalName
     */
    public Signal(String signalDate, String signalName) {
        this.signalDate = new SimpleStringProperty(signalDate);
        this.signalName = new SimpleStringProperty(signalName);
    }

    public String getSignalDate() {
        return signalDate.get();
    }

    public void setSignalDate(String signalDate) {
        this.signalDate.set(signalDate);
    }

    public StringProperty signalDateProperty() {
        return signalDate;
    }

    public String getSignalName() {
        return signalName.get();
    }

    public void setSignalName(String signalName) {
        this.signalName.set(signalName);
    }

    public StringProperty signalNameProperty() {
        return signalName;
    }

}

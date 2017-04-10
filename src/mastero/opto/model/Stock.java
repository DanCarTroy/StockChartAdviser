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
 * Model class for a Stock.
 *
 */
public class Stock {

    private final StringProperty stockSymbol;
    private final StringProperty stockName;
    private final StringProperty street;
    private final IntegerProperty postalCode;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> birthday;

    private XYChart.Series<String,Number> currentDataSeries = new XYChart.Series<String, Number>();

    /**
     * Default constructor.
     */
    public Stock() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     *
     * @param stockSymbol
     * @param stockName
     */
    public Stock(String stockSymbol, String stockName) {
        this.stockSymbol = new SimpleStringProperty(stockSymbol);
        this.stockName = new SimpleStringProperty(stockName);

        // Some initial dummy data, just for convenient testing.
        this.street = new SimpleStringProperty("some street");
        this.postalCode = new SimpleIntegerProperty(1234);
        this.city = new SimpleStringProperty("some city");
        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
    }

    public String getStockSymbol() {
        return stockSymbol.get();
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol.set(stockSymbol);
    }

    public StringProperty stockSymbolProperty() {
        return stockSymbol;
    }

    public String getStockName() {
        return stockName.get();
    }

    public void setStockName(String stockName) {
        this.stockName.set(stockName);
    }

    public StringProperty stockNameProperty() {
        return stockName;
    }

    public String getStreet() {
        return street.get();
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public StringProperty streetProperty() {
        return street;
    }

    public int getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public IntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

    public LocalDate getBirthday() {
        return birthday.get();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }

    public XYChart.Series<String, Number> getChartData(){
		Scanner csvFile;
		DecimalFormat f = new DecimalFormat("###.##");
		ArrayList<Double> closePrice = new ArrayList<>();
		ArrayList<String> dateStrings = new ArrayList<>();
		int dateStringIndex = 0;
		int closePriceIndex = 4;

		getCSVFromYahoo();
		try{
			csvFile = new Scanner(new File("src/mastero/opto/view/tmpData.csv"));
			csvFile.useDelimiter(",");
			csvFile.nextLine();
			while(csvFile.hasNext()){
				String dataString = csvFile.nextLine();
				closePrice.add(Double.parseDouble((f.format(Double.parseDouble(dataString.split(",")[closePriceIndex])))));
				dateStrings.add(dataString.split(",")[dateStringIndex]);
			}
			csvFile.close();
		}
		catch(FileNotFoundException e){
			e.getStackTrace();
		}
		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();
		for (int i = closePrice.size() - 1; i > -1;i--) {
			series.getData().add(new XYChart.Data<String, Number>(dateStrings.get(i), closePrice.get(i)));
		}
		currentDataSeries = copySeries(series);
		return series;
    }

    public XYChart.Series<String, Number> getChartData(int range){

		DecimalFormat f = new DecimalFormat("###.##");

		LocalDate fromDate = LocalDate.now(ZoneId.of( "America/Montreal")).minusDays(range);
		int year = fromDate.getYear();
		int month = fromDate.getMonthValue();
		int day = fromDate.getDayOfMonth();

		XYChart.Series<String,Number> series = new XYChart.Series<String, Number>();

		// YQL (Yahoo Query Language) url --> preparing the url with the range and stock that we want
		String yqlUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20date%2C%20close%20from%20csv%20where%20url%3D'http%3A%2F%2Fichart.finance.yahoo.com%2Ftable.csv%3Fs%3D"
		+this.getStockSymbol()+"%26a%3D"+month+"%26b%3D"+day+"%26c%3D"+year+"'%20and%20columns%3D'date%2Copen%2Chigh%2Clow%2Cclose%2Cvolume%2Cadj_close'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		String JSONstring = getJASON(yqlUrl);
		System.out.println("my jason = "+JSONstring);

		JSONArray arr = null;
		try{

			JSONObject j = new JSONObject(JSONstring);
	        arr = j.getJSONObject("query").getJSONObject("results").getJSONArray("row");


	        // Ending the loop before going through index 0 because index 0 of arr gives back the name of the attributes and not the values.
	        for(int i=arr.length()-1; i > 0; i--)
	        {
	            JSONObject obj = arr.getJSONObject(i);
	            Double closePrice = Double.parseDouble((f.format(Double.parseDouble(obj.getString("close")))));
	            String dateString = obj.getString("date");

	            series.getData().add(new XYChart.Data<String, Number>(dateString, closePrice));

	        }
		}
		catch(JSONException e){
			e.printStackTrace();
		}

		if (series == null){
			System.out.println("Null");
		}
		else
			System.out.println(series.getData().size());
		currentDataSeries = copySeries(series);
		System.out.println(currentDataSeries.getData().size());
		return series;
	}

    public static XYChart.Series<String, Number> copySeries(XYChart.Series<String, Number> target){
		XYChart.Series<String, Number> copy = new XYChart.Series<String, Number>();
		int size = target.getData().size();
		for(int i = 0; i < size; i++){
			copy.getData().add(target.getData().get(i));
		}
		return copy;
	}

    public XYChart.Series<String, Number> getCurrentSeries(){
		return copySeries(currentDataSeries);
	}

    public void getCSVFromYahoo()
    {
    	URL source = null;
		try {
			source = new URL("http://ichart.finance.yahoo.com/table.csv?s="+this.getStockSymbol()+"&ignore=.csv");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	File tmpFile = new File("src/mastero/opto/view/tmpData.csv");
    	try {
			FileUtils.copyURLToFile(source, tmpFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static String getJASON(String url)
    {
    	URLConnection connection = null;
    	URL source = null;
    	String JSONstring = null;

    	try{
    		source = new URL(url);
    	} catch(MalformedURLException e){
    		e.printStackTrace();
    	}

		try {
			connection = source.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			JSONstring = in.readLine();
			// Not necessary because it is returning just one line.
			/*
			String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        {
	            System.out.println(inputLine);
	        }
	        */
			//System.out.println("json is: ");
			//System.out.println(JSONstring);
			//System.out.println("after jason");
	        in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return JSONstring;

    }

}

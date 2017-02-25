package mastero.opto.util;
//import mastero.opto.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class DBConnection /*implements Constants*/ {

	private Connection myCon;

	// Info to connect to the database
	private static final String url = "jdbc:mysql://localhost:3306/stock_adviser_db";
	private static final String user = "root";
	private static final String pwd = "DHCTAMPM1989";

	/**
	 * Default constructor. Creates an instance object that will establish a connection.
	 */
	public DBConnection() {
		myCon = connect();

	}

	/**
	 * Method that establishes connection to the database
	 * @return Connection to the database
	 */
	private Connection connect()
	{
		// Comment this out for older versions of Java and Java's MYSQL driver.
		/*
		try {
			   Class.forName("com.mysql.jdbc.Driver");
			}
			catch(ClassNotFoundException ex) {
			   System.out.println("Error: unable to load driver class!");
			   System.exit(1);
			}
		*/

		Connection myCon = null;

		try
		{
			myCon = DriverManager.getConnection(url, user, pwd);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return myCon;
	}

	/**
	 * Executes a query that expects 1 ResultSet back. Use with SELECT
	 * statements.
	 *
	 * @return A ResultSet object containing the results from the database.
	 */
	public ResultSet executeQuery(String query, Object... values) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = myCon.createStatement();
			rs = stmt.executeQuery(String.format(query, values));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * Use with INSERT, DELETE, UPDATE statements.
	 *
	 * @return The number of rows affected.
	 */
	public int executeUpdate(String query, Object... values) {
		Statement stmt = null;
		try {
			stmt = myCon.createStatement();
			return stmt.executeUpdate(String.format(query, values));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}



	public int executeUpdate(String query) {
		Statement stmt = null;
		try {
			stmt = myCon.createStatement();
			return stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public ResultSet query(String str)
	{
		ResultSet res = null;

		Statement stmt = null;

		try {
			stmt = myCon.createStatement();
			res = stmt.executeQuery(str);
		//	c.commit();
		//	c.close();
		//	s.close();
			System.out.println("query completed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;

	}

}


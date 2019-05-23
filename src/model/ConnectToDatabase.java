package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

public class ConnectToDatabase {
	
	public static Connection getConnection() throws SQLException
	{
		
		try {
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig sqConfig = new SQLiteConfig();  
		    sqConfig.enforceForeignKeys(true);
			return DriverManager.getConnection("jdbc:sqlite:PrisonDatabase.db");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
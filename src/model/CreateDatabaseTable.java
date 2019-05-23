package model;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabaseTable {

	
	private Connection connect;
	CreateDatabaseTable(Connection connect)
	{
		this.connect = connect;
	}
	
	public void createTables() {  
        Statement state;
        String query;

        try {
        	state = connect.createStatement();
        	query = "CREATE TABLE IF NOT EXISTS PRISON_STAFF"
        			+ " (staff_id TEXT PRIMARY KEY,"
        			+ " name TEXT NOT NULL,"
        			+ " age INT NOT NULL,"
        			+ " sex TEXT NOT NULL,"
        			+ " job TEXT NOT NULL)";
        	
            state.executeUpdate(query);
            state.close();
            
            state = connect.createStatement();
        	query = "CREATE TABLE IF NOT EXISTS PRISONERS"
        			+ " (prisoner_id TEXT PRIMARY KEY,"
        			+ " name TEXT NOT NULL,"
        			+ " age INT NOT NULL,"
        			+ " sex TEXT NOT NULL,"
        			+ " offence TEXT NOT NULL,"
        			+ " sentence TEXT NOT NULL,"
        			+ " FOREIGN KEY(offence) REFERENCES OFFENCES (type) ON DELETE CASCADE)";
        			
        	
            state.executeUpdate(query);
            state.close();
            
            state = connect.createStatement();
        	query = "CREATE TABLE IF NOT EXISTS VISITORS"
        			+ " (visitor_id TEXT PRIMARY KEY,"
        			+ " name TEXT NOT NULL,"
        			+ " age INT NOT NULL,"
        			+ " visitedPrisoner TEXT NOT NULL,"
        			+ " FOREIGN KEY (visitedPrisoner) REFERENCES PRISONERS (prisoner_id) ON DELETE CASCADE)";
        	
            state.executeUpdate(query);
            state.close();
            
            state = connect.createStatement();
        	query = "CREATE TABLE IF NOT EXISTS OFFENCES"
        			+ " (type TEXT PRIMARY KEY,"
        			+ " min_sentence ,"
        			+ " max_sentence )";
        	
            state.executeUpdate(query);
            state.close();

 
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}
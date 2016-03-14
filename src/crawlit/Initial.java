package crawlit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Initial {
	
	public static void sqlite_create_province_table(){		
		Connection c = null;
		Statement stmt = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE PROVINCE " + 
			             "(ID INTEGER, " +
					     "NAME CHAR(10)	PRIMARY KEY NOT NULL, "  +
			             "ABBREVIATION	CHAR(10) NOT NULL); ";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();			
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully.");
	}
	
	public static void sqlite_delete_province_table(){		
		Connection c = null;
		Statement stmt = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			stmt = c.createStatement();
			String sql = "DROP TABLE PROVINCE; ";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();			
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully.");
	}
	
	public static void sqlite_create_sceneryspot_table(){		
		Connection c = null;
		Statement stmt = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			stmt = c.createStatement();
			String sql = "CREATE TABLE SCENERYSPOT " + 
			             "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					     "NAME CHAR(10)	NOT NULL, "  +
			             "PROVINCE	CHAR(10) NOT NULL); ";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();			
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully.");
	}
	
	public static void sqlite_delete_sceneryspot_table(){		
		Connection c = null;
		Statement stmt = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			
			stmt = c.createStatement();
			String sql = "DROP TABLE SCENERYSPOT; ";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();			
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully.");
	}

	public static void main(String[] args) {
		
		System.out.println("this function is only executed at once.");
		
		//sqlite_create_province_table();
		//sqlite_create_sceneryspot_table();
		
		//sqlite_delete_province_table();
		//sqlite_delete_sceneryspot_table();

	}

}

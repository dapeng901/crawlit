package crawlit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class Initial {
	
	public static void create_province_table(){		
		System.out.println("  create_province_table() in.");
		
		Connection c = null;
		Statement stmt = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			String sql = "select count(*) from sqlite_master where type = 'table' and name = 'PROVINCE'";		
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.getInt(1) == 0){
				sql = "CREATE TABLE PROVINCE " + 
			             "(NAME CHAR(10)	PRIMARY KEY NOT NULL, "  +
			             "ABBREVIATION	CHAR(10) NOT NULL); ";
				stmt.executeUpdate(sql);				
			}		
			
			stmt.close();
			rs.close();
			c.commit();
			c.close();		
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("  create_province_table() out.");
	}
	
	public static void delete_province_table(){		
		System.out.println("  delete_province_table() in.");

		Connection c = null;
		Statement stmt = null;
		
		try{			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			String sql = "select count(*) from sqlite_master where type = 'table' and name = 'PROVINCE'";		
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.getInt(1) == 1){
				sql = "DROP TABLE PROVINCE; ";
				stmt.executeUpdate(sql);				
			}		
			
			stmt.close();
			rs.close();
			c.commit();
			c.close();		
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("  delete_province_table() out.");
	}
	
	public static void create_sceneryspot_table(){	
		System.out.println("  create_sceneryspot_table() in.");

		Connection c = null;
		Statement stmt = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			String sql = "select count(*) from sqlite_master where type = 'table' and name = 'SCENERYSPOT'";		
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.getInt(1) == 0){
				sql = "CREATE TABLE SCENERYSPOT " + 
			             "(NAME CHAR(10)  PRIMARY KEY NOT NULL, "  +
			             "PROVINCE	CHAR(10) NOT NULL); ";
				stmt.executeUpdate(sql);				
			}		
			
			stmt.close();
			rs.close();
			c.commit();
			c.close();			
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("  create_sceneryspot_table() in.");
	}
	
	public static void delete_sceneryspot_table(){	
		
		System.out.println("  delete_sceneryspot_table() in.");

		Connection c = null;
		Statement stmt = null;
		Statement stmt1 = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			c.setAutoCommit(false);
			
			stmt = c.createStatement();
			String sql = "select count(*) from sqlite_master where type = 'table' and name = 'SCENERYSPOT'";		
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.getInt(1) == 1){
				sql = "DROP TABLE SCENERYSPOT; ";
				stmt.executeUpdate(sql);				
			}		
			
			stmt.close();
			rs.close();
			c.commit();
			c.close();			
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
		System.out.println("  delete_sceneryspot_table() out.");

	}

	public static void main(String[] args) {
		
		System.out.println("main() in.");
		
		delete_province_table();
	    delete_sceneryspot_table();
	    
		create_province_table();
		create_sceneryspot_table();

		System.out.println("main() out.");


	}

}

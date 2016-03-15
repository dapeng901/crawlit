/*
 * ��ҳ->����
 */

package crawlit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CrawlSceneryFromMfw {
	
	public void getProvinceInfo() throws Exception {
		System.out.println("  getProvinceInfo() in.");
		
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://www.mafengwo.cn/gonglve/");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);  
        //System.out.println(html);

       	String regEx = "<li><a href=\"/gonglve/mdd-(.*?)-0-0-1.html#list\">([\\s\\S]*?)\\([0-9]*?\\)</a></li>";
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(html);
    	
    	int count = 0;
    	while(mat.find()){
    		count = count + 1;
    		//System.out.println(mat.group(2));  	
    		
    		Connection c = null;
    		Statement stmt = null;
    		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			c = DriverManager.getConnection("jdbc:sqlite:test.db");
    			c.setAutoCommit(false);    			   			
    			stmt = c.createStatement();  			
    			
    			// �ȿ�����ʡ���Ƿ������ݿ����Ѿ�����   
    			boolean is_exit = false;
    			ResultSet rs = stmt.executeQuery("SELECT NAME FROM PROVINCE;");	
    			while(rs.next()){		
    				String name = rs.getString("NAME");    				
    				if(name.equals(mat.group(2))){
    					//System.out.println("this province already exits.");
    					is_exit = true;
    				}    				
    			}  			
    			
    			// ������ݿ���û�и�ʡ�ݣ������  	
    			if (!is_exit){
    				String sql = "INSERT INTO PROVINCE (NAME, ABBREVIATION)" +
    					     "VALUES ( '" + mat.group(2) + "', '" + mat.group(1) + "');";
    				stmt.executeUpdate(sql);
    			}
				
				rs.close();
				stmt.close();
				c.commit();				
				c.close();
    		}catch(Exception e ){
    			System.err.println(e.getClass().getName()+":"+e.getMessage());
    			System.exit(0);
    		}
    	}
        httpclient.close();

        System.out.println("  getProvinceInfo() out.");

    }
	
	public void getScenerySpotInfo(String abbr) throws Exception {		
		System.out.println("  getScenerySpotInfo() in.");
        
		// ���������ʡ����д�����ɸ�ʡ������ľ��������ַ��������ַ����http��ҳ
		String province_name = "";
		String addr = "http://www.mafengwo.cn/gonglve/mdd-" + abbr + "-0-0-1.html#list";		
		
		// ����ʡ����д�����ʡ������
		Connection c1 = null;
		Statement stmt1 = null;
		
		try{
			Class.forName("org.sqlite.JDBC");
			c1 = DriverManager.getConnection("jdbc:sqlite:test.db");
			c1.setAutoCommit(false);
			System.out.println("opened database susccessfully.");
			
			stmt1 = c1.createStatement();

			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM PROVINCE WHERE ABBREVIATION = '" + abbr + "';");
			//ResultSet rs1 = stmt1.executeQuery("SELECT * FROM PROVINCE;");

			while(rs1.next()){
				province_name = rs1.getString("NAME");				
				System.out.println("NAME = " + province_name);
				System.out.println();
			}
			rs1.close();
			stmt1.close();
			c1.close();
		}catch (Exception e){
			System.err.println(e.getClass().getName() +": " + e.getMessage());
			System.exit(0);
		}
				
		CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(addr);
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);  
        System.out.println(html);
		
		// ����ҳ����������ʽ��������ȡ��Ϣ
        //<a href="/gonglve/mdd-10487.html" title="����" target="_blank">
        //<a href="/gonglve/mdd-10186.html" title="����" target="_blank">
        //<a href="/gonglve/mdd-12711.html" title="����" target="_blank">
        //<a href="/gonglve/mdd-10807.html" title="����" target="_blank">
        //String regEx = "<li><a href=\"/gonglve/mdd-(.*?)-0-0-1.html#list\">([\\s\\S]*?)\\([0-9]*?\\)</a></li>";
        String regEx = "<a href=\"/gonglve/mdd-([0-9]*?).html\" title=\"([\\s\\S]*?)\" target=\"_blank\">";
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(html);
    	
    	int count = 0;
    	while(mat.find()){
    		count = count + 1;
    		//System.out.println(mat.groupCount());
    		//System.out.println(mat.group());
    		System.out.println(mat.group(2));
    		
     		Connection c = null;
    		Statement stmt = null;
    		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			c = DriverManager.getConnection("jdbc:sqlite:test.db");

    			c.setAutoCommit(false);
    			
    			//System.out.println("opened database successfully.");
    			
    			stmt = c.createStatement();
    			
    			
    			// �ȿ����þ����Ƿ������ݿ����Ѿ�����   			
    			ResultSet rs = stmt.executeQuery("SELECT NAME FROM SCENERYSPOT;");
    				
    			boolean is_exit = false;
    			while(rs.next()){
    				
    				String name = rs.getString("NAME");
    				
    				if(name.equals(mat.group(2))){
    					System.out.println("this province already exits.");
    					System.out.println();
    					is_exit = true;
    				}    				
    			}    			
    			
    			// ������ݿ���û�иþ��㣬�����  	
    			if (!is_exit){  
    				String sql = "INSERT INTO SCENERYSPOT (NAME, PROVINCE)" +
    					     "VALUES ('" + mat.group(2) + "', '" + province_name + "');";
    				stmt.executeUpdate(sql);
    			}
				
				rs.close();
				stmt.close();
				c.commit();
				
				c.close();
    		}catch(Exception e ){
    			System.err.println(e.getClass().getName()+":"+e.getMessage());
    			System.exit(0);
    		}
    	}
        httpclient.close();
        
		System.out.println("  getScenerySpotInfo() out.");
    }
}

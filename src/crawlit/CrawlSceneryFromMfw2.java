/*
 * 首页-目的地-输入-景点
 */

package crawlit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CrawlSceneryFromMfw2 {
	

	public static void getScenerySpotInfo() throws Exception {		
		System.out.println("  getScenerySpotInfo() in.");
		
		String province = "";
		String city = "";
        
		String addr = "http://www.mafengwo.cn/jd/10684/gonglve.html";		
					
		CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(addr);
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);  
        System.out.println(html);
		
		// 对网页进行正则表达式解析，提取信息
        
        // <span class="hd"><a href="/travel-scenic-spot/mafengwo/14387.html" target="_blank">江苏<i></i></a></span>
        String regEx1 = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/14387.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        Pattern pat1 = Pattern.compile(regEx1);
    	Matcher mat1 = pat1.matcher(html);
    	
    	if(mat1.find()){
    		province =  mat1.group(1) ;
    	}
    	
    	// <li><a href="/travel-scenic-spot/mafengwo/10684.html" target="_blank">南京</a></li>
    	String regEx2 = "<li><a href=\"/travel-scenic-spot/mafengwo/10684.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)</a></li>";		
        Pattern pat2 = Pattern.compile(regEx2);
    	Matcher mat2 = pat2.matcher(html);
    	
    	if(mat2.find()){
    		city =  mat2.group(1) ;
    	}
        
        // data-tags="13714"><strong>夫子庙-秦淮风光带</strong>
        //String regEx ="<a href=\"/poi/([0-9]*?).html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)</a>";
        String regEx ="data-tags=\"([0-9]*?)\"><strong>([\u4E00-\u9FA5,-]*?)</strong>";
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(html);
    	
    	int count = 0;
    	while(mat.find()){
    		count = count + 1;
    		System.out.println(mat.group(2));
    		
     		Connection c = null;
    		Statement stmt = null;
    		
    		try{
    			Class.forName("org.sqlite.JDBC");
    			c = DriverManager.getConnection("jdbc:sqlite:test.db");
    			c.setAutoCommit(false);    			   			
    			stmt = c.createStatement();    			
    			
    			// 先看看该景点是否在数据库中已经存在   		
    			boolean is_exit = false;
    			ResultSet rs = stmt.executeQuery("SELECT NAME FROM SCENERYSPOT;");    			
    			while(rs.next()){    				
    				String name = rs.getString("NAME");
    				if(name.equals(mat.group(2))){
    					System.out.println("this province already exits.");
    					is_exit = true;
    				}    				
    			}    			
    			
    			// 如果数据库中没有该景点，则添加  	
    			if (!is_exit){  
    				String sql = "INSERT INTO SCENERYSPOT (NAME, CITY, PROVINCE)" +
    					     "VALUES ('" + mat.group(2) + "', '" + city + "','" + province +"');";
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
	
	public static void main(String[] args) throws Exception {
		System.out.println("main() in"); 
		
		getScenerySpotInfo();		
		
		System.out.println("main() out"); 
	}

}

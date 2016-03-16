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

	public static void getCitySceneryList() throws Exception {		
		System.out.println("  getCitySceneryList() in.");
		
		String name = "", tele = "", webaddr = "", transfer = "",
				ticket = "", opentime = "", spendtime = "",
				addr = "", city = "", province = "";
					
		CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://www.mafengwo.cn/jd/10684/gonglve.html");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);  
        System.out.println(html);
		
		// 提取省        
        // <span class="hd"><a href="/travel-scenic-spot/mafengwo/14387.html" target="_blank">江苏<i></i></a></span>
        String regEx1 = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/14387.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        Pattern pat1 = Pattern.compile(regEx1);
    	Matcher mat1 = pat1.matcher(html);    	
    	if(mat1.find()){
    		province =  mat1.group(1) ;
    	}
    	
    	// 提取市
    	// <li><a href="/travel-scenic-spot/mafengwo/10684.html" target="_blank">南京</a></li>
    	String regEx2 = "<li><a href=\"/travel-scenic-spot/mafengwo/10684.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)</a></li>";		
        Pattern pat2 = Pattern.compile(regEx2);
    	Matcher mat2 = pat2.matcher(html);
    	if(mat2.find()){
    		city =  mat2.group(1) ;
    	}
        
    	// 提取景点列表
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
    				name = rs.getString("NAME");
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
        
		System.out.println("  getCitySceneryList() out.");
    }
	
	public static void getScenerySpotInfo(String input_addr) throws Exception {		
		System.out.println("  getScenerySpotInfo() in.");
		
		String name = "", introduction = "", tele = "", webaddr = "", 
				transfer = "", ticket = "", opentime = "", spendtime = "",
				addr = "", city = "", province = "";

		CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(input_addr);
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);        
        html = html.replaceAll("[\\t\\n\\r]", "");//将内容区域的回车换行去除
        System.out.println(html);
		
        // 提取名字
        // <div class="share" id="_j_sharecnt" data-content="" data-title="夫子庙-秦淮风光带"
        String regEx = "<div class=\"share\" id=\"_j_sharecnt\" data-content=\"\" data-title=\"([\\s\\S]*?)\"";		
        Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(html);    	
    	if(mat.find()){
    		name =  mat.group(1);
    		//System.out.println(name);
    	}
        
		// 提取省        
        // <span class="hd"><a href="/travel-scenic-spot/mafengwo/14387.html" target="_blank">江苏<i></i></a></span>
        regEx = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/14387.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);    	
    	if(mat.find()){
    		province =  mat.group(1);
    		System.out.println(province);
    	}
    	
    	// 提取市
    	// <span class="hd"><a href="/travel-scenic-spot/mafengwo/10684.html">南京<i></i></a></span>
    	regEx = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/10684.html\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		city =  mat.group(1) ;
    		System.out.println(city);
    	}
        
    	// 提取景点介绍
    	// <p data-simrows="2" data-format="1" style="overflow: hidden;max-height: 48px;"><span>夫子庙-秦淮河风光带以夫子庙为中心，秦淮河为纽带，包括瞻园、夫子庙、白鹭洲公园、中华门、以及从桃叶渡至镇淮桥一带的秦淮水上游船和沿河楼阁景观。这里庙市夜景合一，始终是南京最繁华的地方之一，美称"十里珠帘"。</span></p>
    	regEx = "<p data-simrows=\"2\" data-format=\"1\" style=\"overflow: hidden;max-height: 48px;\"><span>([\\s\\S]*?)</span></p>";  	
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		introduction =  mat.group(1) ;
    		//System.out.println(introduction);
    	}
    	
    	// 提取景点电话
    	// <p>025-52219554;025-52230238;025-52377008</p>
    	regEx = "<p>([0-9-;]*?)</p>";  	
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		tele =  mat.group(1);
    		//System.out.println(tele);
    	}
    	
    	// 提取景点网址
    	// <p><a href="http://www.njfzm.net/" target="_blank" rel="nofollow">http://www.njfzm.net/</a></p>
    	regEx = "<p><a href=\"(http://www.[\\s\\S]*?/)\" target=\"_blank\" rel=\"nofollow\">http://www.([\\s\\S]*?)/</a></p>";  	
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		webaddr =  mat.group(1);
    		//System.out.println(webaddr);
    	}
    	
    	// 提取景点交通信息
    	// <p data-simrows="2" style="overflow: hidden;max-height: 48px"><span>公共交通   <br />1、地铁一号线三山街站下   <br />2、公交1、4、7、40、44、49、62、304路到夫子庙（东）站下    <br />3、游2、1、15路到夫子庙（北）站下   <br />4、游4、2、14、16、23、26、33、43、46、63、81、87、101、102、103、106、305路到长乐路站下   <br />5、301路到瞻园路站下</span></p>
    	regEx = "<p data-simrows=\"2\" style=\"overflow: hidden;max-height: 48px\"><span>([公共交通\\s\\S]*?)</span></p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		transfer =  mat.group(1);
    		//System.out.println(transfer);
    	}
    	
    	// 提取景点门票信息
    	// <p data-simrows="2" style="overflow: hidden;max-height: 48px"><span>夫子庙大成殿、江南贡院、王导谢安纪念馆、李香君故居，秦大士故居单独售票分别为30元、20元、8元、16元、8元。白鹭洲公园6:00-17:00：20元，18:00-22:00，40元/位（不看演出）观看《夜泊秦淮》80元/位。</span></p>
    	regEx = "<span class=\"label\">门票</span>([\\s]*?)<p data-simrows=\"2\" style=\"overflow: hidden;max-height: 48px\"><span>([\\s\\S]*?)</span></p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		ticket =  mat.group(2);
    		//System.out.println(ticket);
    	}
    	
    	// 提取景点开放时间
    	// <p data-simrows="2" style="overflow: hidden;max-height: 48px"><span>夫子庙大成殿、江南贡院、王导谢安纪念馆、李香君故居，秦大士故居单独售票分别为30元、20元、8元、16元、8元。白鹭洲公园6:00-17:00：20元，18:00-22:00，40元/位（不看演出）观看《夜泊秦淮》80元/位。</span></p>
    	regEx = "<span class=\"label\">开放时间</span>([\\s]*?)<p data-simrows=\"2\" style=\"overflow: hidden;max-height: 48px\"><span>([\\s\\S]*?)</span></p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		opentime =  mat.group(2);
    		//System.out.println(opentime);
    	}
    	
    	// 提取用时参考
    	// <span class="label">用时参考</span>        <p>4小时-1天</p>
    	regEx = "<span class=\"label\">用时参考</span>([\\s]*?)<p>([\\s\\S]*?)</p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		spendtime =  mat.group(2);
    		//System.out.println(spendtime);
    	}
    	
    	// 提取景点位置
    	// <h2>景点位置</h2>        <div style="margin-top: 5px;font-size: 14px">南京市秦淮区龙蟠中路(东起东水关淮清桥秦淮水亭，越过文德桥，直到中华门城堡延伸直西水关的内秦淮河地带)</div>
    	regEx = "<h2>景点位置</h2>([\\s]*?)<div style=\"margin-top: 5px;font-size: 14px\">([\\s\\S]*?)</div>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		addr =  mat.group(2);
    		//System.out.println(addr);
    	}

    	// 写入数据库
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
    			name = rs.getString("NAME");
    			if(name.equals(mat.group(2))){
    				System.out.println("this province already exits.");
    				is_exit = true;
    			}    				
    		}
    			
    		// 如果数据库中没有该景点，则添加  	
    		if (!is_exit){  
    			String sql = "INSERT INTO SCENERYSPOT (NAME,INTRODUCTION,TELE,WEBADDR,TRANSFER,TICKET,OPENTIME,SPENDTIME,ADDR, CITY,PROVINCE)" +
    					     "VALUES ('" + name + "', '" + introduction + "', '" + tele + "','" + webaddr + "','" + transfer + "','" + ticket + "','" + opentime + "','" + spendtime + "','" + addr + "','" + city + "','" + province +"');";
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

        httpclient.close();
        
		System.out.println("  getScenerySpotInfo() out.");
    }
	
	public static void main(String[] args) throws Exception {
		System.out.println("main() in"); 
		
		//getCitySceneryList();	
		getScenerySpotInfo("http://www.mafengwo.cn/poi/2293952.html");
		
		System.out.println("main() out"); 
	}

}

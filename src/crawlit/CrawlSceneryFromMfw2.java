/*
 * ��ҳ-Ŀ�ĵ�-����-����
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
		
		// ��ȡʡ        
        // <span class="hd"><a href="/travel-scenic-spot/mafengwo/14387.html" target="_blank">����<i></i></a></span>
        String regEx1 = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/14387.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        Pattern pat1 = Pattern.compile(regEx1);
    	Matcher mat1 = pat1.matcher(html);    	
    	if(mat1.find()){
    		province =  mat1.group(1) ;
    	}
    	
    	// ��ȡ��
    	// <li><a href="/travel-scenic-spot/mafengwo/10684.html" target="_blank">�Ͼ�</a></li>
    	String regEx2 = "<li><a href=\"/travel-scenic-spot/mafengwo/10684.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)</a></li>";		
        Pattern pat2 = Pattern.compile(regEx2);
    	Matcher mat2 = pat2.matcher(html);
    	if(mat2.find()){
    		city =  mat2.group(1) ;
    	}
        
    	// ��ȡ�����б�
        // data-tags="13714"><strong>������-�ػ�����</strong>
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
    			
    			// �ȿ����þ����Ƿ������ݿ����Ѿ�����   		
    			boolean is_exit = false;
    			ResultSet rs = stmt.executeQuery("SELECT NAME FROM SCENERYSPOT;");    			
    			while(rs.next()){    				
    				name = rs.getString("NAME");
    				if(name.equals(mat.group(2))){
    					System.out.println("this province already exits.");
    					is_exit = true;
    				}    				
    			}    			
    			
    			// ������ݿ���û�иþ��㣬�����  	
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
        html = html.replaceAll("[\\t\\n\\r]", "");//����������Ļس�����ȥ��
        System.out.println(html);
		
        // ��ȡ����
        // <div class="share" id="_j_sharecnt" data-content="" data-title="������-�ػ�����"
        String regEx = "<div class=\"share\" id=\"_j_sharecnt\" data-content=\"\" data-title=\"([\\s\\S]*?)\"";		
        Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(html);    	
    	if(mat.find()){
    		name =  mat.group(1);
    		//System.out.println(name);
    	}
        
		// ��ȡʡ        
        // <span class="hd"><a href="/travel-scenic-spot/mafengwo/14387.html" target="_blank">����<i></i></a></span>
        regEx = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/14387.html\" target=\"_blank\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);    	
    	if(mat.find()){
    		province =  mat.group(1);
    		System.out.println(province);
    	}
    	
    	// ��ȡ��
    	// <span class="hd"><a href="/travel-scenic-spot/mafengwo/10684.html">�Ͼ�<i></i></a></span>
    	regEx = "<span class=\"hd\"><a href=\"/travel-scenic-spot/mafengwo/10684.html\">([\u4E00-\u9FA5,-]*?)<i></i></a></span>";		
        pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		city =  mat.group(1) ;
    		System.out.println(city);
    	}
        
    	// ��ȡ�������
    	// <p data-simrows="2" data-format="1" style="overflow: hidden;max-height: 48px;"><span>������-�ػ��ӷ����Է�����Ϊ���ģ��ػ���ΪŦ��������հ԰�������������޹�԰���л��š��Լ�����Ҷ��������һ�����ػ�ˮ���δ����غ�¥�󾰹ۡ���������ҹ����һ��ʼ�����Ͼ�����ĵط�֮һ������"ʮ������"��</span></p>
    	regEx = "<p data-simrows=\"2\" data-format=\"1\" style=\"overflow: hidden;max-height: 48px;\"><span>([\\s\\S]*?)</span></p>";  	
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		introduction =  mat.group(1) ;
    		//System.out.println(introduction);
    	}
    	
    	// ��ȡ����绰
    	// <p>025-52219554;025-52230238;025-52377008</p>
    	regEx = "<p>([0-9-;]*?)</p>";  	
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		tele =  mat.group(1);
    		//System.out.println(tele);
    	}
    	
    	// ��ȡ������ַ
    	// <p><a href="http://www.njfzm.net/" target="_blank" rel="nofollow">http://www.njfzm.net/</a></p>
    	regEx = "<p><a href=\"(http://www.[\\s\\S]*?/)\" target=\"_blank\" rel=\"nofollow\">http://www.([\\s\\S]*?)/</a></p>";  	
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		webaddr =  mat.group(1);
    		//System.out.println(webaddr);
    	}
    	
    	// ��ȡ���㽻ͨ��Ϣ
    	// <p data-simrows="2" style="overflow: hidden;max-height: 48px"><span>������ͨ   <br />1������һ������ɽ��վ��   <br />2������1��4��7��40��44��49��62��304·������������վ��    <br />3����2��1��15·������������վ��   <br />4����4��2��14��16��23��26��33��43��46��63��81��87��101��102��103��106��305·������·վ��   <br />5��301·��հ԰·վ��</span></p>
    	regEx = "<p data-simrows=\"2\" style=\"overflow: hidden;max-height: 48px\"><span>([������ͨ\\s\\S]*?)</span></p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		transfer =  mat.group(1);
    		//System.out.println(transfer);
    	}
    	
    	// ��ȡ������Ʊ��Ϣ
    	// <p data-simrows="2" style="overflow: hidden;max-height: 48px"><span>�������ɵ���Ϲ�Ժ������л������ݡ�������ʾӣ��ش�ʿ�ʾӵ�����Ʊ�ֱ�Ϊ30Ԫ��20Ԫ��8Ԫ��16Ԫ��8Ԫ�������޹�԰6:00-17:00��20Ԫ��18:00-22:00��40Ԫ/λ�������ݳ����ۿ���ҹ���ػ���80Ԫ/λ��</span></p>
    	regEx = "<span class=\"label\">��Ʊ</span>([\\s]*?)<p data-simrows=\"2\" style=\"overflow: hidden;max-height: 48px\"><span>([\\s\\S]*?)</span></p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		ticket =  mat.group(2);
    		//System.out.println(ticket);
    	}
    	
    	// ��ȡ���㿪��ʱ��
    	// <p data-simrows="2" style="overflow: hidden;max-height: 48px"><span>�������ɵ���Ϲ�Ժ������л������ݡ�������ʾӣ��ش�ʿ�ʾӵ�����Ʊ�ֱ�Ϊ30Ԫ��20Ԫ��8Ԫ��16Ԫ��8Ԫ�������޹�԰6:00-17:00��20Ԫ��18:00-22:00��40Ԫ/λ�������ݳ����ۿ���ҹ���ػ���80Ԫ/λ��</span></p>
    	regEx = "<span class=\"label\">����ʱ��</span>([\\s]*?)<p data-simrows=\"2\" style=\"overflow: hidden;max-height: 48px\"><span>([\\s\\S]*?)</span></p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		opentime =  mat.group(2);
    		//System.out.println(opentime);
    	}
    	
    	// ��ȡ��ʱ�ο�
    	// <span class="label">��ʱ�ο�</span>        <p>4Сʱ-1��</p>
    	regEx = "<span class=\"label\">��ʱ�ο�</span>([\\s]*?)<p>([\\s\\S]*?)</p>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		spendtime =  mat.group(2);
    		//System.out.println(spendtime);
    	}
    	
    	// ��ȡ����λ��
    	// <h2>����λ��</h2>        <div style="margin-top: 5px;font-size: 14px">�Ͼ����ػ��������·(����ˮ�ػ������ػ�ˮͤ��Խ���ĵ��ţ�ֱ���л��ųǱ�����ֱ��ˮ�ص����ػ��ӵش�)</div>
    	regEx = "<h2>����λ��</h2>([\\s]*?)<div style=\"margin-top: 5px;font-size: 14px\">([\\s\\S]*?)</div>";
    	pat = Pattern.compile(regEx);
    	mat = pat.matcher(html);
    	if(mat.find()){
    		addr =  mat.group(2);
    		//System.out.println(addr);
    	}

    	// д�����ݿ�
    	Connection c = null;
    	Statement stmt = null;
    	
    	try{
    		Class.forName("org.sqlite.JDBC");
    		c = DriverManager.getConnection("jdbc:sqlite:test.db");
    		c.setAutoCommit(false);    			   			
    		stmt = c.createStatement();    			
    			
    		// �ȿ����þ����Ƿ������ݿ����Ѿ�����   		
    		boolean is_exit = false;
    		ResultSet rs = stmt.executeQuery("SELECT NAME FROM SCENERYSPOT;");    			
    		while(rs.next()){    				
    			name = rs.getString("NAME");
    			if(name.equals(mat.group(2))){
    				System.out.println("this province already exits.");
    				is_exit = true;
    			}    				
    		}
    			
    		// ������ݿ���û�иþ��㣬�����  	
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

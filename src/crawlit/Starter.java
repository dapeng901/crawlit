package crawlit;

public class Starter {

	public static void main(String[] args) throws Exception {
		System.out.println("main() in"); 
		
		CrawlSceneryFromMfw crawlMfw = new CrawlSceneryFromMfw();
		crawlMfw.getProvinceInfo();
		crawlMfw.getScenerySpotInfo("yn");
		
		System.out.println("main() out"); 
	}
}

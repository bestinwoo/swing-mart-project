package crawler;
import db.GoodsVO;
import db.DB;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class Crawler {
	// 지마켓 카테고리별 URL 코드
	private String fresh = "100000020";
	private String processed = "100000036", health = "100000068", drink = "100000094";
	
	
	public Crawler() {
		//getData();
	}
	
	
	public static void main(String[] args) {
		new Crawler();
	}
	/** JSoup을 이용하여 지마켓에서 상품 데이터 DB에 넣기 */
	private void getData() {
		String URL = "http://corners.gmarket.co.kr/BestSellers?viewType=C&largeCategoryCode=" + drink;
		ArrayList<GoodsVO> goods = new ArrayList<>();
		DB.init();
		Document doc;
		try {
			doc = Jsoup.connect(URL).get();
			Elements elem = doc.select("div.best-list");
			for(int i = 0; i < elem.select("a.itemname").size(); i++) {
				if(elem.select("a.itemname").get(i).text().isEmpty()) continue;
				String goodsName = elem.select("a.itemname").get(i).text();
				String priceStr = elem.select("div.s-price").select("strong").get(i).text();
				priceStr = priceStr.replace(",", "");
				int price = Integer.parseInt(priceStr.substring(0, priceStr.length() - 1));
				
				String url = elem.select("div.thumb img").get(i).attr("data-original");				
				URL imgUrl = new URL(url);
				BufferedImage jpg = ImageIO.read(imgUrl);
				File file = new File("images/" + (i+296) + ".jpg");
				ImageIO.write(jpg, "jpg", file);
				
				System.out.println(i);
				goods.add(new GoodsVO(i+296, goodsName, price, new Random().nextInt(100)+10, "커피/음료"));
			}
			System.out.println(goods);
			DB.insertGoods(goods);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error");
		}
	}
}

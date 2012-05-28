package viprammo.data;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import viprammo.log.MyHandler;
import viprammo.util.GLOBAL_CONFIG;

public class ImageCreater {

	private Logger logger = Logger.getLogger(ImageCreater.class.getName());
	
	private static ImageCreater instance = new ImageCreater();

	private Map<String, Image> img_map = new HashMap<String, Image>();

	private String[][] map_data;
	
	private ImageCreater() {
		logger.addHandler(new MyHandler());
		logger.setLevel(GLOBAL_CONFIG.LOG_LEVEL);
		
		//キャラ画像読み込み
		this.readPicture();
		//マップの木とか川の画像読み込み
		this.readMapObjectPicture();
		//マップデータのCSV読み込み
		this.readMapData();
	}
	
	public static ImageCreater getInstance() {
		return instance;
	}
	
	public String[][] getMapdata() {
		return this.map_data;
	}
	
	private void readPicture() {
		
		String[] chara = {"b", "g"};
		
		for (String char_prefix : chara) {
			
			String[] files = {"w0", "w1", "w2", "a1", "a2", "s0", "s1", "s2", "d1", "d2"};
			String base_url = "http://www.viprammo.com/vipra/pic/";
			String url = null;
		
			for (String s : files) {
				url = base_url + char_prefix + "_" + s  + ".png";
				try {
					this.img_map.put(char_prefix + "_" + s, ImageIO.read(new URL(url)));
					logger.info(url);
				} catch (MalformedURLException e) {
					logger.severe(e.getMessage());
				} catch (IOException e) {
					logger.severe(e.getMessage());
				}
			}	
			
		}
		
		logger.info("画像読み込み完了");
		
	}

	private void readMapObjectPicture() {
		
		String[] files = { "W0", "K0"};
		String base_url = "http://www.viprammo.com/vipra/map/pic/";
		String url = null;
		
		for (String s : files) {
			url = base_url + s  + ".png";
			try {
				this.img_map.put(s, ImageIO.read(new URL(url)));
				logger.info(url);
			} catch (MalformedURLException e) {
				logger.severe(e.getMessage());
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}	
			
		logger.info("マップオブジェクト画像読み込み完了");
		
	}
	
	private void readMapData() {
		
		logger.info("マップデータ読み込み開始");
		
		//とりあえず今はmapタイル数固定（エリアごとに必要なサイズはサーバから渡すとかにする）
		//ホントに適当に作ったmapデータなんで・・・
		int tile_x = 24;
		int tile_y = 23;
		
		this.map_data = new String[tile_x][tile_y];
		
		String url = "http://www.viprammo.com/vipra/map/default.map";
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new URL(url).openStream()));
			
			String buff;
			int y = 0;
			while ((buff = br.readLine()) != null) {

				int x = 0;
				for (String s : buff.split(",")) {
					this.map_data[x][y] = s;
					x++;
				}
				y++;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("マップデータ読み込み完了");
	}
	
	public Image getImg(String prefix) {
		
		logger.finest("画像=" + prefix);
		return this.img_map.get(prefix);
	
	}
}

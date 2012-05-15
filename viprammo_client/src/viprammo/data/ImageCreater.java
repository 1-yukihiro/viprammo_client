package viprammo.data;

import java.awt.Image;
import java.io.IOException;
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

	
	private ImageCreater() {
		logger.addHandler(new MyHandler());
		logger.setLevel(GLOBAL_CONFIG.LOG_LEVEL);
		this.read();
	}
	
	public static ImageCreater getInstance() {
		return instance;
	}
	
	private void read() {
		
		String[] chara = {"b", "g"};
		
		for (String char_prefix : chara) {
			
			String[] files = {"w0", "w1", "w2", "a1", "a2", "s0", "s1", "s2", "d1", "d2"};
			String base_url = "http://118.243.3.245/vipra/pic/";
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

	public Image getImg(String prefix) {
		
		logger.finest("画像=" + prefix);
		return this.img_map.get(prefix);
	
	}
}

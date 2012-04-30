package viprammo.data;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import viparammo.log.MyHandler;

public class ImageCreater {

	private Logger logger = Logger.getLogger(ImageCreater.class.getName());
	
	private static ImageCreater instance = new ImageCreater();

	private Map<String, Image> img_map = new HashMap<String, Image>();

	
	private ImageCreater() {
		logger.addHandler(new MyHandler());
		this.read();
	}
	
	public static ImageCreater getInstance() {
		return instance;
	}
	
	private void read() {
		
		String[] files = {"w1", "a1", "a2", "s1", "s2", "d1", "d2"};
		String base_url = "http://118.243.3.245/vipra/pic/";
		String url = null;
		
		for (String s : files) {
			url = base_url + s;
			try {
				this.img_map.put(s, ImageIO.read(new URL(url)));
				logger.info(url);
			} catch (MalformedURLException e) {
				logger.severe(e.getMessage());
			} catch (IOException e) {
				logger.severe(e.getMessage());
			}
		}

		logger.info("‰æ‘œ“Ç‚İ‚İŠ®—¹");
		
	}

	public Image getImg(String prefix) {
		
		logger.info("“Ç‚İ‚İ‰æ‘œ=" + prefix);
		return this.img_map.get(prefix);
	
	}
}

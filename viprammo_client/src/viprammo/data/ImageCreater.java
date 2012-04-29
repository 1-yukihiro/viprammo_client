package viprammo.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageCreater {

	private static ImageCreater instance = new ImageCreater();
	
	Image i_w1;
	Image i_a1;
	Image i_a2;
	Image i_s1;
	Image i_s2;
	Image i_d1;
	Image i_d2;
	
	private ImageCreater() {
		this.read();
	}
	
	public static ImageCreater getInstance() {
		return instance;
	}
	
	private void read() {
		
		try {
			this.i_w1 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/w1.png"));
			this.i_a1 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/a1.png"));
			this.i_a2 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/a2.png"));
			this.i_s1 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/s1.png"));
			this.i_s2 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/s2.png"));
			this.i_d1 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/d1.png"));
			this.i_d2 = ImageIO.read(new URL("http://118.243.3.245/vipra/pic/d2.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Image getImg(String prefix) {
		if (prefix.equals("w1")) {
			return this.i_w1;
		} else if (prefix.equals("a1")) {
			return this.i_a1;
		} else if (prefix.equals("a2")) {
			return this.i_a2;
		} else if (prefix.equals("s1")) {
			return this.i_s1;
		} else if (prefix.equals("s2")) {
			return this.i_s2;
		} else if (prefix.equals("d1")) {
			return this.i_d1;
		} else if (prefix.equals("d2")) {
			return this.i_d2;
		} else {
			return null;
		}
	}
}

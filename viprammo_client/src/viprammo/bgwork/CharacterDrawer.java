package viprammo.bgwork;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import viprammo.data.ImageCreater;
import viprammo.gui.MainWindow;

/**
 * キャラクターの描画を行うクラス（CharanterDrawer.getInstance().draw()）されると描画
 * 将来的には全ての描画系処理（チャットも含め）をこいつで行う予定
 * 各所でnewされたら困るんでシングルトン
 * @author Yukihiro
 *
 */
public class CharacterDrawer {

	//インスタンス
	private static CharacterDrawer instance = new CharacterDrawer();

	//使われることないようprivateに（正確にはgetInstanceを最初に行ったとき実行されるけど）
	private CharacterDrawer() {
	}

	/**
	 * インスタンスゲッター
	 * @return 唯一のこいつのインスタンスを返す（つまりどこで呼ばれても同じこいつにアクセス）
	 */
	public static CharacterDrawer getInstance() {
		return instance;
	}

	/**
	 * このクラスの本体処理
	 * サーバから来た指令を実行する
	 * @param val ここには今のところサーバから来たコマンド文字列を与える）
	 */
	public void draw(String val) {

		
		String[] valsp1 = val.replaceAll("\r\n", "").split("-");

		String[] valsplit = valsp1[1].split(",");

		int count = Integer.parseInt(valsp1[0]);
		
		//バッファ作成
		final Image buff_img = MainWindow.getInstance().panel.createImage(
				MainWindow.getInstance().panel.getWidth(), MainWindow.getInstance().panel.getHeight());
		
		Graphics2D g2d = (Graphics2D) buff_img.getGraphics();
		
		for (int i = 0; i < count; i++) {
			
			String method = valsplit[0+(i*4)+i];
			String name = valsplit[1+(i*4)+i];
			String muki = valsplit[4+(i*4)+i];
			
			int x = Integer.parseInt(valsplit[2+(i*4)+i]);
			int y = Integer.parseInt(valsplit[3+(i*4)+i]);
			String pmuki = muki;
			
			if (method.equals("M")) {

				//バッファに向きに応じた画像を描画する（画像は読み込み済みのデータを書くためImageObserverは不要）
				g2d.drawImage(ImageCreater.getInstance().getImg(pmuki), x, y, null);
				//名前を表示する
				g2d.drawString(name, x+35, y+35);
				
			}
		}
		
		//描画済みのバッファをウィンドウに描画する
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow.getInstance().panel.getGraphics().drawImage(buff_img, 0, 0, MainWindow.getInstance().panel);
			}
		});
		
	}
}

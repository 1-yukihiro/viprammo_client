package viprammo.bgwork;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import viprammo.data.ImageCreater;
import viprammo.gui.MainWindow;
import viprammo.message.CharacterModifMessage;
import viprammo.message.ChatMessage;
import viprammo.message.CommandMessage;
import viprammo.message.Message;
import viprammo.message.MessageKIND;

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
	 * CommandMessage版の描画処理
	 * @param c_message
	 */
	public void draw(CommandMessage c_message) {
		
		System.out.printf("pnal_width=%d, panel_height=%d\n", MainWindow.getInstance().panel.getWidth(), MainWindow.getInstance().panel.getHeight());
		//キャラクタ情報の書換えフラグ
		boolean character_write_flag = false;

		final Image buff_img = this.mapDraw();
		Graphics2D g2d = (Graphics2D) buff_img.getGraphics();
		
		//受信したメッセージを分解（それぞれのメッセージ種別によって判定してるが良いパターン捜索中）
		for (Message message : c_message.getMessageList()) {
			
			switch (message.getKIND()) {
			case MessageKIND.KIND_CHAT_MESSAGE:
				ChatMessage cm = (ChatMessage) message;
				MainWindow.getInstance().writeChat(cm.getMessage_str());
				break;
			case MessageKIND.KIND_CHARACTER_MODIF:
				CharacterModifMessage cmod_message = (CharacterModifMessage) message;
				character_write_flag = true;
				
				//自分の状態変更であった場合
				if (cmod_message.user.equals(MainWindow.getInstance().name)) {
					//ゲームマネージャに最新の自分の状態を入れておく
					GameManager.getInstance().setCharacterModifMessage(cmod_message);
				}
				
				//g2d.drawImage(ImageCreater.getInstance().getImg(cmod_message.getCharacter_prefix() + "_" + cmod_message.getMuki()), cmod_message.getX(), cmod_message.getY(), null);
				g2d.drawImage(ImageCreater.getInstance().getImg(cmod_message.getCharacter_prefix() + "_" + cmod_message.getMuki()), 292, 190, null);
				//g2d.drawString(cmod_message.getUser(), cmod_message.getX()+35, cmod_message.getY() + 35);
				g2d.drawString(cmod_message.getUser(), 292+35, 190 + 35);
				break;
			}
			
		}
		
		//キャラ位置情報の書き込みをした場合は描画
		if (character_write_flag) {
		//描画済みのバッファをウィンドウに描画する
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					MainWindow.getInstance().panel.getGraphics().drawImage(buff_img, 0, 0, MainWindow.getInstance().panel);
				}
			});
		}
		
	}
	
	private BufferedImage mapDraw() {
		
		if (GameManager.getInstance().getCharacterModif() == null) {
			return null;			
		}
		
		//新しいバッファ作成
		BufferedImage bimg_new = new BufferedImage(MainWindow.getInstance().panel.getWidth(),
												   MainWindow.getInstance().panel.getHeight(),
												   BufferedImage.TYPE_INT_ARGB); 
		
		Graphics2D g2d_map = bimg_new.createGraphics();
		g2d_map.setColor(Color.white);
		g2d_map.fillRect(0, 0, 584, 379);
		String[][] data = ImageCreater.getInstance().getMapdata();
		
		//必要範囲を見つける
		int x = GameManager.getInstance().getCharacterModif().getX();
		int y = GameManager.getInstance().getCharacterModif().getY();
		
		//画像ファイル中の自分の座標
		int x_pos = x / 64;
		int y_pos = y / 64;
		
		//表示すべきx,yの画像ファイル中の起点
		int x_pos_start = x_pos <= 5 ? 0: x_pos - 5; 
		int y_pos_start = y_pos <= 3 ? 0: y_pos - 3;
		
		System.out.printf("x_pos=%d, y_pos=%d, xps=%d, yps=%d\n", x_pos, y_pos, x_pos_start, y_pos_start);
		
		//バッファに画像を描画する
		for (int i = x_pos_start; i < x_pos + 5; i++) {
			for (int j = y_pos_start; j < y_pos + 3; j++) {
				if (!data[i][j].equals("P")) {
					g2d_map.drawImage(ImageCreater.getInstance().getImg(data[j][i]), j * 64, i* 64, null);
				}
			}
		}
		
		return bimg_new;
	}
}

package viprammo.bgwork;

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
		//自キャラ書換えフラグ
		boolean mycharacter_move_flag = false;
		
		//バッファ作成
		final Image buff_img = MainWindow.getInstance().panel.createImage(
				MainWindow.getInstance().panel.getWidth(), MainWindow.getInstance().panel.getHeight());
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
					mycharacter_move_flag = true;
					//ゲームマネージャに最新の自分の状態を入れておく
					GameManager.getInstance().setCharacterModifMessage(cmod_message);
				}
				
				g2d.drawImage(ImageCreater.getInstance().getImg(cmod_message.getCharacter_prefix() + "_" + cmod_message.getMuki()), cmod_message.getX(), cmod_message.getY(), null);
				g2d.drawString(cmod_message.getUser(), cmod_message.getX()+35, cmod_message.getY() + 35);
				break;
			}
			
		}
		
		//自キャラの移動を行った場合はmapの表示領域に変更があるかもしれないので描画
		if (mycharacter_move_flag) {			
			
			//新しいバッファ作成
			BufferedImage bimg_new = new BufferedImage(24*64, 23*64, BufferedImage.TYPE_INT_ARGB); 
			
			Graphics2D g2d_map = bimg_new.createGraphics();
			
			String[][] data = ImageCreater.getInstance().getMapdata();
			//バッファに画像を描画する
			for (int i = 0; i < 23; i++) {
				for (int j = 0; j < 24; j++) {
					if (!data[j][i].equals("P")) {
						g2d_map.drawImage(ImageCreater.getInstance().getImg(data[j][i]), j * 64, i* 64, null);
					}
				}
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
	
	private void mapDraw() {
		
	}
}

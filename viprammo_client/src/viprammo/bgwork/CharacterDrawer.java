package viprammo.bgwork;

import java.awt.Graphics2D;
import java.awt.Image;

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
		
		//キャラクタ情報の書換えフラグ
		boolean character_write_flag = false;
		
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
				
				g2d.drawImage(ImageCreater.getInstance().getImg(cmod_message.getMuki()), cmod_message.getX(), cmod_message.getY(), null);
				g2d.drawString(cmod_message.getUser(), cmod_message.getX()+35, cmod_message.getY() + 35);
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
}

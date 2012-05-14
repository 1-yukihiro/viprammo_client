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
		
		boolean m_flag = false;
		for (int i = 0; i < count; i++) {
			
			String method = valsplit[0+(i*4)+i];
			String name = valsplit[1+(i*4)+i];
			String cmd_1 = valsplit[4+(i*4)+i];
			
			int x = Integer.parseInt(valsplit[2+(i*4)+i]);
			int y = Integer.parseInt(valsplit[3+(i*4)+i]);
			
			//位置情報なら
			if (method.equals("M")) {
				m_flag = true;
				String muki = cmd_1;
				//バッファに向きに応じた画像を描画する（画像は読み込み済みのデータを書くためImageObserverは不要）
				g2d.drawImage(ImageCreater.getInstance().getImg(muki), x, y, null);
				//名前を表示する
				g2d.drawString(name, x+35, y+35);
				
				//チャットメッセージなら
			} else if (method.equals("C")) {
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				sb.append(name);
				sb.append("] ");
				sb.append(cmd_1);
				//チャットウィンドウに書き込む
				MainWindow.getInstance().writeChat(sb.toString());
			}
		}
		
		//キャラ位置情報の書き込みをした場合は描画
		if (m_flag) {
		//描画済みのバッファをウィンドウに描画する
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					MainWindow.getInstance().panel.getGraphics().drawImage(buff_img, 0, 0, MainWindow.getInstance().panel);
				}
			});
		}
		
		
	}
}

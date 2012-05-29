package viprammo.bgwork;

import viprammo.gui.MainWindow;
import viprammo.message.CharacterModifMessage;

/**
 * ゲームマネージャクラス
 * @author Yukihiro
 *
 */
public class GameManager {

	private CharacterModifMessage cmod_message;
	
	public static GameManager instance = new GameManager();
	
	private GameManager() {
		this.cmod_message = new CharacterModifMessage();
		this.cmod_message.user = MainWindow.getInstance().name;
		this.cmod_message.setX(300);
		this.cmod_message.setY(300);
	}
	
	public static GameManager getInstance() {
		return instance;
	}
	
	public void setCharacterModifMessage(CharacterModifMessage cmod) {
		this.cmod_message = cmod;
	}
	
	public CharacterModifMessage getCharacterModif() {
		return this.cmod_message;
	}
}

package viprammo.bgwork;

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

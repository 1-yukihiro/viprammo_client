import java.util.logging.Logger;

import viprammo.data.ImageCreater;
import viprammo.gui.FirstWindow;
import viprammo.log.MyHandler;


public class VIPMMO {

	/**
	 * ここから開始　メイン
	 * @param args
	 */
	public static void main(String[] args) {

		Logger logger = Logger.getLogger(VIPMMO.class.getName());
		logger.addHandler(new MyHandler());
		
		logger.info("起動しました");
		logger.info("画像の読み込み開始");
			
		//名前入力画面（ログイン画面）表示
		logger.info("ログイン画面表示");
		new FirstWindow();

	}

}

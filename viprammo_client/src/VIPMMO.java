import java.util.logging.Logger;

import viparammo.log.MyHandler;
import viprammo.data.ImageCreater;
import viprammo.gui.FirstWindow;


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
		
		//画像の読み込み（getInstanceすれば自動的に裏でnewされて画像がメモリに入る）
		ImageCreater.getInstance();
		
		//名前入力画面（ログイン画面）表示
		logger.info("ログイン画面表示");
		
		new FirstWindow();

	}

}

import viprammo.data.ImageCreater;
import viprammo.gui.FirstWindow;


public class VIPMMO {

	/**
	 * ここから開始　メイン
	 * @param args
	 */
	public static void main(String[] args) {

		//画像の読み込み（getInstanceすれば自動的に裏でnewされて画像がメモリに入る）
		ImageCreater.getInstance();
		
		//名前入力画面（ログイン画面）表示
		new FirstWindow();

	}

}

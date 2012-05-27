package viprammo.bgwork;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import viprammo.log.MyHandler;
import viprammo.message.CommandMessage;
import viprammo.util.VipraUtil;

/**
 * データ受信クラス
 * フォーマットは　バイナリで(バイト数[int 4b])(フラグ[byte 1b])(データ[byte nb])....
 * フラグは将来的に圧縮有無とかを表す予定
 * @author Yukihiro
 *
 */
public class TCPSocketReceiver extends Thread {

	/**
	 * 描画処理クラス
	 */
	DisplayUpdateWorker duw = new DisplayUpdateWorker();
	
	/**
	 * ソケット
	 */
	Socket socket;
	
	/**
	 * 処理スレッド達
	 */
	InputStreamWorker inputworker;
	OutputStreamWorker outputworker;
	
	String name;
	
	/**
	 * ロガー
	 */
	Logger logger = Logger.getLogger(TCPSocketReceiver.class.getName());
	
	public void run() {

		//ログ画面へのHandler
		logger.addHandler(new MyHandler());
		
		this.inputworker.start();
		this.outputworker.start();
		this.duw.start();
		
	}
	
	/**
	 * コンストラクタ（キャラの名前を与える）
	 * @param name キャラの名前
	 */
	public TCPSocketReceiver(String name) {
		
		this.name = name;
		
		try {
			socket = new Socket("118.243.3.245", 10001);
			//socket = new Socket("127.0.0.1", 10001);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			//それぞれ受信送信用の処理スレッドを作る
			this.inputworker = new InputStreamWorker(is, this.duw);
			this.outputworker = new OutputStreamWorker(os);
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ソケットの入力を処理するクラス
	 * @author Yukihiro
	 *
	 */
	private class InputStreamWorker extends Thread {
		
		DataInputStream dis;
		ObjectInputStream ois;
		DisplayUpdateWorker displayupdateworker;
		
		public InputStreamWorker(InputStream inputstream, DisplayUpdateWorker du) {
			dis = new DataInputStream(inputstream);
			displayupdateworker = du;
		}
		
		public void run() {
			
			try {
				ois = new ObjectInputStream(dis);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			while (true) {
				
				try {

					CommandMessage cm = (CommandMessage)ois.readObject();
					//CharacterDrawer.getInstance().draw(cm);
					displayupdateworker.addCommand(cm);
					
				} catch (IOException e) {
					logger.severe("ソケット処理でIOエラー");
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	/**
	 * ソケットへの出力を処理するクラス
	 * @author Yukihiro
	 *
	 */
	private class OutputStreamWorker extends Thread {
		
		ObjectOutputStream oos;
		
		public OutputStreamWorker(OutputStream os) {
			try {
				oos = new ObjectOutputStream(os);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void run() {
			
			while (true) {
				
			}
			
		}

		public void send(CommandMessage cm) {
			try {
				oos.writeObject(cm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void send(CommandMessage cm) {
		this.outputworker.send(cm);
	}
	
}

package viprammo.bgwork;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	 * ソケット
	 */
	Socket socket;
	
	/**
	 * 処理スレッド達
	 */
	InputStreamWorker inputworker;
	OutputStreamWorker outputworker;
	
	/**
	 * ロガー
	 */
	Logger logger = Logger.getLogger(TCPSocketReceiver.class.getName());
	
	public void run() {

		//ログ画面へのHandler
		logger.addHandler(new MyHandler());
		
		this.inputworker.start();
		this.outputworker.start();
		
	}
	
	/**
	 * コンストラクタ（キャラの名前を与える）
	 * @param name キャラの名前
	 */
	public TCPSocketReceiver(String name) {
		
		try {
			socket = new Socket("118.243.3.245", 10001);
			//socket = new Socket("127.0.0.1", 10001);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			//それぞれ受信送信用の処理スレッドを作る
			this.inputworker = new InputStreamWorker(is);
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
		
		public InputStreamWorker(InputStream inputstream) {
			dis = new DataInputStream(inputstream);
		}
		
		public void run() {
			
			int length = 0;
			byte ident = 0;
			
			while (true) {
				
				try {
					length = dis.readInt();
					ident = dis.readByte();
					System.out.println(String.format("ヘッダー読み込み length=%d, ident=%d", length, ident));
					logger.finest(String.format("ヘッダー読み込み length=%d, ident=%d", length, ident));
					
					byte[] buff = new byte[length];
					dis.readFully(buff);
					
					
				} catch (IOException e) {
					logger.severe("ソケット処理でIOエラー");
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
		
		DataOutputStream dos;
		
		public OutputStreamWorker(OutputStream os) {
			dos = new DataOutputStream(os);
		}
		
		public void run() {
			
			while (true) {
				
				try {
					Thread.sleep(300l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}

		public void send(byte[] b) {
			
			try {
				this.dos.writeInt(b.length);
				this.dos.writeByte(1);
				this.dos.write(b, 0, b.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void send(CommandMessage cm) {
		ByteArrayOutputStream bytearray = null;
		
		try {
			bytearray = new ByteArrayOutputStream();
			ObjectOutputStream oss = new ObjectOutputStream(bytearray);
			oss.writeObject(cm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buff = bytearray.toByteArray();
		
		int length = buff.length;
		byte ident = 1;
		ByteBuffer bf = ByteBuffer.allocate(5 + buff.length);
		bf.putInt(length);
		bf.put(ident);
		bf.put(buff);
		
		this.send(bf.array());
	}
	
	public void send(byte[] b) {
		this.outputworker.send(b);
	}
	
}

package viprammo.bgwork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import viprammo.util.GLOBAL_CONFIG;
import viprammo.util.VipraUtil;

/**
 * データ受信クラス
 * フォーマットは　バイナリで(バイト数[int 4b])(フラグ[byte 1b])(データ[byte nb])....
 * フラグは将来的に圧縮有無とかを表す予定
 * @author Yukihiro
 *
 */
public class TCPSocketReceiver extends Thread {

	Socket socket;
	InputStream is;
	
	//ストップフラグ　trueで受信停止する
	boolean stopflag = false;
	
	public void run() {

		//受信バッファ
		byte[] buff = new byte[GLOBAL_CONFIG.CLIENT_BUFFER_SIZE];
		
		//処理に使用するバッファーDirectの方が高速
		ByteBuffer buffer = ByteBuffer.allocateDirect(GLOBAL_CONFIG.CLIENT_BUFFER_SIZE * 10);
		
		//初回の読み込みバイト数カウンタ
		int read_num = 0;		
		
		//データ中のバイト列数
		int length = 0;
		
		//将来用識別
		byte ident = 0;

		//データ部取り出す用の配列
		byte[] data_bu;
		
		while (!this.stopflag) {
		
			try {
				
				read_num = is.read(buff);
				System.out.println("readnum=" + read_num);
				
				buffer.put(buff, 0, read_num);
				buffer.flip();
				
				//取得しなければならないlengthが0（つまりまだヘッダー読んでない）の状態で
				//なおかつbufferのリミットが5バイトより少ない（つまりヘッダーが無い）状態であればさらにputが必要なのでcontinue
				//ここでは断片化で受信した4byteとかがあるかもしれない
				if ((length == 0) && (buffer.limit() < 5)) {
					continue;
				}
				
				//ヘッダーを読んで居ない状態でヘッダー分以上のデータがあればヘッダーを読む
				if ((length == 0) && (buffer.limit() >= 5)) {
					
					//バッファーを読むときはflip(ここでpos0limitはputしたとこまでに)
					//buffer.flip();
					
					length = buffer.getInt();
					ident = buffer.get();

				}

				
				//-------------------- ここに来る時点で必ずlengthは入ってる ----------------------------

				
				//バッファーのリミットがlength以上ならデータを読み込んで処理に回す
				if (buffer.remaining() >= length) {
					
					System.out.println("remai="+buffer.remaining());
					
					//データ部用の配列作る
					data_bu = new byte[length];
					//データ取得
					buffer.get(data_bu);

					System.out.printf("length=%d, ident=%d, data=%d\n", length, ident, data_bu.length);
					
					//データ取得が終了したら追記できるようにしておく
					//buffer.compact();
					//lengthに0入れる
					length = 0;
					System.out.println(buffer.limit());
					
				}
				
				buffer.compact();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * 旧受信処理
	 */
	private void rcv() {
		
		byte[] buff = new byte[1];
		ArrayList<Byte> al = new ArrayList<Byte>();
		
		int rnum = 0;
		byte mae = 0;
		try {
			while (true) {
				
				rnum = is.read(buff);
				if (rnum == -1) {
					break;
				}
				
				System.out.println(buff[0]);
				
				if (buff[0] == 10) {
					if (mae == 13) {
						al.add(buff[0]);
						//改行受信でコマンドデリミター 
						CharacterDrawer.getInstance().draw(new String(VipraUtil.CollectionsByteToPbyte(al), "UTF-8"));
						al.clear();
						mae = 0;
					} else {
						al.add(buff[0]);
						mae = 0;
					}
				} else if (buff[0] == 13) {
					mae = 13;
					al.add(buff[0]);
				} else {
					mae = 0;
					al.add(buff[0]);
				}


				
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("kireta");
	}
	
	public TCPSocketReceiver(String name) {
		
		try {
			//socket = new Socket("118.243.3.245", 10001);
			socket = new Socket("127.0.0.1", 10001);
			OutputStream os = socket.getOutputStream();
			os.write(new String("IAM"+name+"\r\n").getBytes());
			os.flush();

			is = socket.getInputStream();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

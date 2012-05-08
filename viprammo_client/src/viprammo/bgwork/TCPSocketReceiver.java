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
		
		//データ格納用リスト
		List<Byte> data = new ArrayList<Byte>();
		
		//初回の読み込みバイト数カウンタ
		int read_num = 0;		
		
		//データ中のバイト列数
		int length = 0;
		
		//将来用識別
		byte ident = 0;
		
		//読み込み中フラグ
		boolean now_reading = false;
		
		while (!this.stopflag) {
			
			try {
				if ((read_num = is.read(buff)) != -1) {
					
					//とりあえずリストに受信データ格納
					for (int i = 0; i < read_num; i++) {
						data.add(buff[i]);
					}				
					
					//データ部読み込み中なら
					if (now_reading) {
						
						//データの長さ＋ヘッダー分の長さ以上ならデータ部は全てあるのでbyte[]化して処理に回す
						if (data.size() >= length + 5) {
							
							byte[] data_byte = new byte[length];
							for (int i = 0; i < length; i++) {
								data_byte[i] = data.get(i+5);
								data.remove(i+5); //ここでデータ部は消える
							}
							
							//ヘッダー部を消す
							for (int i = 0; i < 5; i++) {
								data.remove(i);
							}
							
							//data_byte = データ 次処理に回す
							
							//データ読み込み中は終了
							now_reading = false;
							
						}
						
						//データ部読み込み中でなければ
					} else {
						length = ByteBuffer.wrap(new byte[] {data.get(0), data.get(1), data.get(2), data.get(3)}).getInt();
						ident = data.get(4);
						now_reading = true;
					}
				}
				
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
			socket = new Socket("118.243.3.245", 10001);
			//socket = new Socket("172.17.10.100", 10001);
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

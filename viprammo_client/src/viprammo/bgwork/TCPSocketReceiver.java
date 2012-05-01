package viprammo.bgwork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import viprammo.util.VipraUtil;

public class TCPSocketReceiver extends Thread {

	Socket socket;
	InputStream is;
	
	public void run() {
		
		rcv();
		
	}
	
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

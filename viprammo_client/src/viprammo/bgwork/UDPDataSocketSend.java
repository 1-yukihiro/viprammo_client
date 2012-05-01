package viprammo.bgwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPDataSocketSend {

	private static DatagramSocket dgsock;
	
	private static InetSocketAddress inetsocketaddr = new InetSocketAddress("118.243.3.245", 10500);
	//private static InetSocketAddress inetsocketaddr = new InetSocketAddress("172.17.10.100", 10500);
	
	public static void send(byte[] data) {
		try {
			dgsock = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(data, data.length, inetsocketaddr);
			dgsock.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

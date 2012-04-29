package viprammo.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import viprammo.bgwork.TCPSocketReceiver;
import viprammo.bgwork.UDPDataSocketSend;
import viprammo.data.UserCharacter;

public class MainWindow implements KeyListener {

	public JFrame frame;
	public String name;
	public JPanel panel;
	
	private static MainWindow instance = new MainWindow();
	
	public static MainWindow getInstance() {
		return instance;
	}
	
	private MainWindow() {
		
		frame = new JFrame("VIPRAMMO");
		panel = new JPanel();
		
		panel.setDoubleBuffered(true);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.addKeyListener(this);
		
	}
	
	public void show() {
		TCPSocketReceiver tcpr = new TCPSocketReceiver(this.name);
		tcpr.start();
		frame.setVisible(true);
	}
	
	public void keyPressed(KeyEvent arg0) {
		
		
		if (arg0.getSource().equals(this.frame)) {
			
			char key_char = arg0.getKeyChar();

			System.out.println(key_char);
			StringBuilder sb = new StringBuilder();
			sb.append(this.name);
			sb.append(",M,");
			sb.append(key_char);
			sb.append("\r\n");
			System.out.println(sb.toString());
			UDPDataSocketSend.send(sb.toString().getBytes());

		} else {
			return;
		}
		
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}
}

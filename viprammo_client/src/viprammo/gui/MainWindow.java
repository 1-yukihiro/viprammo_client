package viprammo.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import viprammo.bgwork.TCPSocketReceiver;
import viprammo.bgwork.UDPDataSocketSend;

public class MainWindow implements KeyListener, ActionListener {

	public JFrame frame;
	public String name;
	public JPanel panel;
	public JMenuBar menubar = this.menubarCreate();
	
	private static MainWindow instance = new MainWindow();
	
	public static MainWindow getInstance() {
		return instance;
	}
	
	private MainWindow() {
		
		frame = new JFrame("VIPRAMMO");
		frame.setJMenuBar(this.menubar);
		
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
	
	
	private JMenuBar menubarCreate() {
		
		JMenuBar menubar = new JMenuBar();
		JMenu menu_game = new JMenu("ゲーム");
		JMenu menu_devel = new JMenu("開発");
		
		JMenuItem menuitem_exit = new JMenuItem("終了");
		menuitem_exit.addActionListener(this);
		
		JMenuItem menuitem_log = new JMenuItem("ログ");
		menuitem_log.addActionListener(this);
		
		menu_game.add(menuitem_exit);
		menu_devel.add(menuitem_log);
		
		menubar.add(menu_game);
		menubar.add(menu_devel);
		
		return menubar;
		
	}
	
	public void keyPressed(KeyEvent arg0) {
		
		System.out.println(arg0.getSource());
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

	public void actionPerformed(ActionEvent arg0) {
		
		String action_cmd = arg0.getActionCommand();
		if (action_cmd.equals("終了")) {
			System.exit(0);
		} else if (action_cmd.equals("ログ")) {
			JMenuItem item = (JMenuItem)arg0.getSource();
			item.setEnabled(false);
			LogWindow.getInstance().show();
		}
		
	}
}

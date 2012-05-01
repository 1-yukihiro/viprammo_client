package viprammo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.omg.IOP.Encoding;

import viprammo.bgwork.TCPSocketReceiver;
import viprammo.bgwork.UDPDataSocketSend;
import viprammo.data.ImageCreater;
import viprammo.log.MyHandler;
import viprammo.util.GLOBAL_CONFIG;

public class MainWindow implements KeyListener, ActionListener, MouseListener {

	public JFrame frame;
	public String name;
	public JPanel panel;
	public JMenuBar menubar = this.menubarCreate();
	
	//チャット部分
	public JPanel chat_panel;
	public JTextField chat_textfield;
	public JList message_list;
	public DefaultListModel listmodel;
		
	private Logger logger = Logger.getLogger(MainWindow.class.getName());
	
	private static MainWindow instance = new MainWindow();
	
	public static MainWindow getInstance() {
		return instance;
	}
	
	private MainWindow() {
		
		//ロガーの設定
		this.logger.setLevel(GLOBAL_CONFIG.LOG_LEVEL);
		this.logger.addHandler(new MyHandler());
		
		//画像の読み込み（getInstanceすれば自動的に裏でnewされて画像がメモリに入る）
		logger.info("画像読み込み");
		
		ImageCreater.getInstance();
		
		frame = new JFrame("VIPRAMMO");
		frame.setJMenuBar(this.menubar);
		
		panel = new JPanel();
		panel.setDoubleBuffered(true);
		panel.setBackground(Color.black);
		//chatgui_initするとチャットパネルが完成
		this.chatgui_init();
		
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(panel, BorderLayout.CENTER);
		frame.add(chat_panel, BorderLayout.SOUTH);
		
		//frame.addKeyListener(this);
		panel.addKeyListener(this);
		panel.setFocusable(true);
		panel.addMouseListener(this);

	}
	
	//コンストラクタごちゃごちゃ嫌なんでチャットウィンドウ作成部分を分ける
	private void chatgui_init() {
		
		chat_panel = new JPanel();
		this.message_list = new JList();
		this.listmodel = new DefaultListModel();
		chat_textfield = new JTextField();
		
		chat_textfield.addKeyListener(this);
		
		JScrollPane scroll_pane = new JScrollPane();
		
		this.message_list.setModel(this.listmodel);
		scroll_pane.getViewport().setView(message_list);
		
		chat_panel.setLayout(new BorderLayout());
		chat_panel.add(scroll_pane, BorderLayout.CENTER);
		chat_panel.add(this.chat_textfield, BorderLayout.SOUTH);
		
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
	
	/**
	 * チャットウィンドウにチャットを出力する
	 * こいつでinvokeLaterしてくれるんで呼び出し先は直で呼んでOK
	 * @param str　出力文字列
	 */
	public void writeChat(final String str) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (listmodel.getSize() >= 300) {
					listmodel.clear();
				}
				listmodel.add(listmodel.getSize(), str);
				message_list.ensureIndexIsVisible(listmodel.getSize()-1);
			}
		});
	}
	
	public void keyPressed(KeyEvent arg0) {
		
		logger.finest(arg0.toString());
		
		//描画エリアからのイベントなら
		if (arg0.getSource().equals(this.panel)) {
			
			char key_char = arg0.getKeyChar();

			logger.finest("key_char=" + key_char);
			StringBuilder sb = new StringBuilder();
			sb.append(this.name);
			sb.append(",M,");
			sb.append(key_char);
			sb.append("\r\n");
			System.out.println(sb.toString());
			UDPDataSocketSend.send(sb.toString().getBytes());

			//チャット入力フィールドからのイベントなら
		} else if (arg0.getSource().equals(this.chat_textfield)) {
			
			char key_char = arg0.getKeyChar();
			//エンター受信で書き込み内容を送信
			if (key_char == '\n') {
				
				System.out.println(System.getProperty("file.encoding"));
				String message = chat_textfield.getText();
				
				message = message.replaceAll(",", "*kinshi*").replaceAll("-", "*kinshi*");
				System.out.println("message="+ message);
				
				//GUI描画に関わる処理
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						chat_textfield.setText("");
					}
				});
				
				logger.finest("message=" + message);
				StringBuilder sb = new StringBuilder();
				sb.append(this.name);
				sb.append(",C,");
				sb.append(message);
				sb.append("\r\n");
				try {
					UDPDataSocketSend.send(sb.toString().getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
			}
			
		} else {
			return;
		}
		
	}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) {}

	public void actionPerformed(ActionEvent arg0) {
		
		logger.finest(arg0.toString());
		
		String action_cmd = arg0.getActionCommand();
		if (action_cmd.equals("終了")) {
			System.exit(0);
		} else if (action_cmd.equals("ログ")) {
			JMenuItem item = (JMenuItem)arg0.getSource();
			item.setEnabled(false);
			LogWindow.getInstance().show();
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		//フィールド描画部分をクリックされたらフォーカスする
		//無いと一生動けなくなる
		this.panel.requestFocus();
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

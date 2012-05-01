package viprammo.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class LogWindow implements ActionListener, WindowListener {

	public JFrame frame;
	public JList list;
	public DefaultListModel listmodel;
	public JButton button;
	
	//またもやシングルトン（GUIはnewされると厄介。とくにこいつはログwindowなんで）
	public static LogWindow instance = new LogWindow();
	
	private LogWindow() {
		
		frame = new JFrame();
		frame.setSize(900, 400);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		list = new JList();
		listmodel = new DefaultListModel();
		list.setModel(listmodel);
		
		JScrollPane scroll_pane = new JScrollPane();
		scroll_pane.getViewport().setView(list);
		
		button = new JButton("ウィンドウ消す");
		button.addActionListener(this);
		
		frame.add(scroll_pane, BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);
		frame.addWindowListener(this);
	}

	/**
	 * インスタンスゲッター
	 * @return
	 */
	public static LogWindow getInstance() {
		return instance;
	}
	
	/**
	 * ログウィンドウにログ出力する
	 * @param logstr ログ文字列
	 */
	public void writeLog(String logstr) {
	
		//でかすぎるとgui吹っ飛ぶかもしれんので500行程度に収める
		if (this.listmodel.size() >= 100) {
			this.listmodel.clear();
		}

		//ここから下はGUIに変更を加える処理
		final String logstr_f = logstr;
		final DefaultListModel dlm = this.listmodel;
		
		//ログの最新行が常に表示されるようにする
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				dlm.add(dlm.getSize(), logstr_f);
				list.setSelectedIndex(dlm.getSize()-1);
				list.ensureIndexIsVisible(dlm.getSize()-1);
			}
		});
		
		
	}

	/**
	 * ログウィンドウを表示する
	 */
	public void show() {
		this.frame.setVisible(true);
	}
	
	/**
	 * ログウィンドウを隠す
	 */
	public void hide() {
		MainWindow.getInstance().menubar.getMenu(1).getItem(0).setEnabled(true);
		this.frame.setVisible(false);
		MainWindow.getInstance().panel.setFocusable(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getActionCommand().equals("ウィンドウ消す")) {
			this.hide();
		}
		
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent arg0) {

	}

	public void windowClosing(WindowEvent arg0) {
		this.hide();
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

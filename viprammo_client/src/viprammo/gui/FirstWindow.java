package viprammo.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import viprammo.util.GLOBAL_CONFIG;

@SuppressWarnings("serial")
public class FirstWindow extends JFrame implements ActionListener {

	private JLabel label;
	private JTextField textfield;
	private JButton button;
	private JComboBox combo = new JComboBox(GLOBAL_CONFIG.LOG_LEVEL_STR);
	private ButtonGroup buttong;
	private JRadioButton radio1;
	private JRadioButton radio2;
	
	public FirstWindow() {
		
		this.setSize(400, 500);
		this.setLayout(new BorderLayout());
		this.textfield = new JTextField();
		this.label = new JLabel("名前を入れてログレベルを選択してください。 ログレベルはINFOがオススメ（FINE系は重くなる可能性有）");
		this.button = new JButton("OK");
		this.button.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//キャラ選択
		radio1 = new JRadioButton("男の子");
		radio2 = new JRadioButton("女の子");
		this.buttong = new ButtonGroup();
		
		this.buttong.add(radio1);
		this.buttong.add(radio2);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(radio1, BorderLayout.EAST);
		panel.add(radio2, BorderLayout.WEST);
		
		this.add(panel, BorderLayout.WEST);
		
		this.add(label, BorderLayout.NORTH);
		this.add(textfield, BorderLayout.CENTER);
		this.add(combo, BorderLayout.EAST);
		this.add(button, BorderLayout.SOUTH);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getSource().equals(this.button)) {
			
			String name_str = this.textfield.getText();
			
			if ((name_str.indexOf("-") != -1) || (name_str.indexOf(",") != -1)) {
				JOptionPane.showMessageDialog(this, new JLabel("<html><font color=\"red\"><b>禁止文字列が入っています！カンマとハイフンはダメです。</b></font>"));
				return;
			}
			
			this.setLogLevel();
			MainWindow.getInstance().name = this.textfield.getText();
			MainWindow.getInstance().setCharPrefix(this.getCharacterPrefix());
			this.dispose();
			MainWindow.getInstance().show();
		}
	}
	
	private String getCharacterPrefix() {
		if (this.radio1.isSelected()) {
			return "b";
		} else {
			return "g";
		}
	}
	private void setLogLevel() {
		
		String selected_str = (String)this.combo.getSelectedItem();
		if (selected_str.equals("FINEST")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.FINEST;
		} else if (selected_str.equals("FINER")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.FINER;
		} else if (selected_str.equals("FINE")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.FINE;
		} else if (selected_str.equals("CONFIG")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.CONFIG;
		} else if (selected_str.equals("INFO")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.INFO;
		} else if (selected_str.equals("WARNING")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.WARNING;
		} else if (selected_str.equals("SEVERE")) {
			GLOBAL_CONFIG.LOG_LEVEL = Level.SEVERE;
		} else {
			GLOBAL_CONFIG.LOG_LEVEL = Level.INFO;
		}
	}
}

package viprammo.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import viprammo.util.GLOBAL_CONFIG;

@SuppressWarnings("serial")
public class FirstWindow extends JFrame implements ActionListener {

	private JLabel label;
	private JTextField textfield;
	private JButton button;
	private JComboBox<String> combo = new JComboBox<String>(GLOBAL_CONFIG.LOG_LEVEL_STR);
	
	public FirstWindow() {
		
		this.setSize(400, 500);
		this.setLayout(new BorderLayout());
		this.textfield = new JTextField();
		this.label = new JLabel("名前を入れてログレベルを選択してください。 ログレベルはINFOがオススメ（FINE系は重くなる可能性有）");
		this.button = new JButton("OK");
		this.button.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			this.dispose();
			MainWindow.getInstance().show();
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

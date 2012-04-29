package viprammo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import viprammo.data.ImageCreater;

@SuppressWarnings("serial")
public class FirstWindow extends JFrame implements ActionListener {

	private JLabel label;
	private JTextField textfield;
	private JButton button;
	
	public FirstWindow() {
		this.setSize(300, 500);
		this.setLayout(new BorderLayout());
		this.textfield = new JTextField();
		this.label = new JLabel("名前を入れてください");
		this.button = new JButton("OK");
		this.button.addActionListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(label, BorderLayout.NORTH);
		this.add(textfield, BorderLayout.CENTER);
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
			
			MainWindow.getInstance().name = this.textfield.getText();
			this.dispose();
			MainWindow.getInstance().show();
		}
	}
}

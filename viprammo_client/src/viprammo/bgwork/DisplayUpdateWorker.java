package viprammo.bgwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import viprammo.message.CommandMessage;

public class DisplayUpdateWorker extends Thread {

	//処理を入れるリスト（一応同期処理しとく）
	private ArrayList<CommandMessage> command = new ArrayList<CommandMessage>();
	
	public DisplayUpdateWorker() {
		Collections.synchronizedCollection(command);
	}
	
	@Override
	public void run() {
		
		while (true) {
			
			for (int i = 0; i < this.command.size(); i++) {
				CharacterDrawer.getInstance().draw(this.command.get(i));
				this.command.remove(i);
			}
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}

	public void addCommand(CommandMessage cm) {
		this.command.add(cm);
	}
	
}

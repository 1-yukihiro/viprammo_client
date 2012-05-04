import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.msgpack.MessagePack;

import viprammo.gui.FirstWindow;
import viprammo.log.MyHandler;
import viprammo.message.CharacterModifMessage;
import viprammo.message.CommandMessage;
import viprammo.message.MessageHeader;


public class VIPMMO {

	/**
	 * ここから開始　メイン
	 * @param args
	 */
	public static void main(String[] args) {

		Test();
		System.exit(99);
		
		Logger logger = Logger.getLogger(VIPMMO.class.getName());
		logger.addHandler(new MyHandler());
		
		logger.info("起動しました");

		//名前入力画面（ログイン画面）表示
		logger.info("ログイン画面表示");
		new FirstWindow();

	}

	public static void Test() {
		CommandMessage c_message = new CommandMessage();
		c_message.setMessageHeader(new MessageHeader("TEST", "172.17.10.1"));
		
		for (int i = 0; i < 5; i++) {
			CharacterModifMessage msg = new CharacterModifMessage();
			msg.setUser("TESTUSER");
			msg.setX(10);
			msg.setY(20);

			c_message.addMessage(msg);
		}
		
		byte[] buff = MessagePack.pack(c_message);
		System.out.println("length="+  buff.length);
		CommandMessage modosu = MessagePack.unpack(buff, CommandMessage.class);
		
		System.out.println(modosu.getMessageHeader().getMessage_string());
		System.out.println(modosu.getMessageList().size());
		System.out.println(modosu.getMessageList().get(3).getUser());
		
	}
	
	private static Font createFontByFile(String file) {
		
		  Font font = null;
		  InputStream is = null;
		  try {
		    is = new FileInputStream(file);
		    font = (Font.createFont(Font.TRUETYPE_FONT, is)).deriveFont(11.0f);
		    is.close();
		  }catch(IOException ioe) {
		    ioe.printStackTrace();
		  }catch(FontFormatException ffe) {
		    ffe.printStackTrace();
		  }finally{
		    if(is!=null) {
		      try{
		        is.close();
		      }catch(IOException ioex) {
		        ioex.printStackTrace();
		      }
		    }
		  }
		  return font;

	}
}

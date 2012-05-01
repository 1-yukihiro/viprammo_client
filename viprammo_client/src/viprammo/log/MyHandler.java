package viprammo.log;

import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import viprammo.gui.LogWindow;

/**
 * GUIログコンソールにログを出力するハンドラー
 * @author Yukihiro
 *
 */
public class MyHandler extends Handler {
	
	@Override
	public void close() throws SecurityException {

	}

	@Override
	public void flush() {

	}

	@Override
	public void publish(LogRecord arg0) {
		if (isLoggable(arg0)) {
			
			StringBuilder sb = new StringBuilder();
			sb.append("<html>");
			sb.append("<font color=\"");
			if ((arg0.getLevel() == Level.FINE) ||
				(arg0.getLevel() == Level.FINER) ||
				(arg0.getLevel() == Level.FINEST)) {
				sb.append("black");
			} else if ((arg0.getLevel() == Level.CONFIG) ||
					   (arg0.getLevel() == Level.INFO)) {
				sb.append("blue");
			} else {
				sb.append("red");
			}
			sb.append("\">");
			
			sb.append(new Date(arg0.getMillis())); sb.append(" -- ");
			sb.append(arg0.getSourceClassName()); sb.append(" -- ");
			sb.append(arg0.getSourceMethodName()); sb.append(" -- ");
			sb.append(arg0.getMessage());
			sb.append("</font></html>");
			
			LogWindow.getInstance().writeLog(sb.toString());
		}
	}

}

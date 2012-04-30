package viparammo.log;

import java.util.Date;
import java.util.logging.Handler;
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
			sb.append(new Date(arg0.getMillis())); sb.append(" -- ");
			sb.append(arg0.getSourceClassName()); sb.append(" -- ");
			sb.append(arg0.getSourceMethodName()); sb.append(" -- ");
			sb.append(arg0.getMessage());
			LogWindow.getInstance().writeLog(sb.toString());
		}
	}

}

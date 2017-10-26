package cn.cjp.utils;

import java.io.IOException;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 可利用占位符将日志写入到不同文件
 * 
 * @author sucre
 *
 */
public class SimpleRollingFileAppender extends RollingFileAppender {

	private boolean writerReady = false;

	@Override
	public void activateOptions() {
		if (fileName != null) {
			String msg = "延期初始化 " + fileName;
			LogLog.debug(msg);
			System.err.println(msg);
		} else {
			// LogLog.error("File option not set for appender ["+name+"].");
			LogLog.warn("File option not set for appender [" + name + "].");
			LogLog.warn("Are you using FileAppender instead of ConsoleAppender?");
		}
	}

	@Override
	public void append(LoggingEvent event) {
		if (!writerReady) {
			try {
				setFile(fileName.replace("{name}", event.getLoggerName()), fileAppend, bufferedIO, bufferSize);
				writerReady = true;
			} catch (IOException e) {
				errorHandler.error("setFile(" + fileName + "," + fileAppend + ") call failed.", e,
						ErrorCode.FILE_OPEN_FAILURE);
			}
		}
		event.getLoggerName();
		if (!checkEntryConditions()) {
			return;
		}
		subAppend(event);
	}

}

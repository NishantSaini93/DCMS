package TestCasesAndCheckers;
/**
 * Logs events to the respective server or manager log files
 */
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LogEvent {
	private Logger logger;
	private String msg = "";
	String file = "";

	public LogEvent(String file) {
		this.file = file;
		logger = Logger.getLogger(file);
	}

	public synchronized void setMessage(String msg) {
		this.msg = msg;
		FileHandler writeFile;
		try{
			writeFile = new FileHandler(file + ".log", true);
			logger.addHandler(writeFile);
			SimpleFormatter formatter = new SimpleFormatter();
			writeFile.setFormatter(formatter);
			logger.setUseParentHandlers(false); //may throw Security Exception
			synchronized (this) {
				logger.info(this.getMessage());
			}
			writeFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public String getMessage() {
		return msg;
	}

}


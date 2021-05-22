package co.stag.proj.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;


public class Debugger {
    private static Logger logger;
    private static FileHandler handler = null;

    static {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            int limit = 200000;//200 KB
            int numLogFiles = 5;
            try {
                File logDir = new File("./logs/");
                if (!(logDir.exists())) {
                    logDir.mkdir();
                }
                handler = new FileHandler("logs/APPLog_" + sdf.format(new Date()) + ".txt", limit, numLogFiles, true);
                logger = Logger.getLogger("APPLog_");
                handler.setFormatter(new Formatter() {
                    public String format(LogRecord rec) {
                        StringBuffer buf = new StringBuffer(1000);
                        //buf.append(new java.util.Date());
                        //buf.append('\n');
                        buf.append(formatMessage(rec));
                        buf.append('\n');
                        return buf.toString();
                    }

                });
                //handler.setFormatter(new SimpleFormatter());
                logger.addHandler(handler);

            } catch (Exception E) {
                System.out.println("failed to create log file");
            }
        } finally {

        }
    }

    public static void println(String msg) {
        logger.log(Level.INFO, msg);
    }
}//end

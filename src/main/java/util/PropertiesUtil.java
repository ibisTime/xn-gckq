package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties props;
    static {
        props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("找不到config.properties文件", e);
        } catch (IOException e) {
            throw new RuntimeException("读取config.properties文件出错", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static final class Config {

        public static String XN_GCHF_URL = props.getProperty("XN_GCHF_URL");

        public static String SN = props.getProperty("SN");

        public static String IP = props.getProperty("IP");

        public static String PORT = props.getProperty("PORT");

        public static String DoorIP = props.getProperty("DoorIP");

        public static String Score = props.getProperty("Score");

        public static String AttendProcesserPath = props
            .getProperty("AttendProcesserPath");

    }
}

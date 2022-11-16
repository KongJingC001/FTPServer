package kj.util;

import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;

import java.io.*;
import java.util.Properties;

@Log
public class PropertiesUtil {


    private static final String ftpConfigPath = "ftp-config.properties";

    /**
     * 项目配置
     */
    private static Properties config;

    static {
        // 用静态代码块加载项目配置
        try (InputStream inputStream = Resources.getResourceAsStream(ftpConfigPath)) {
            config = new Properties();
            config.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertiesUtil() {}

    public static String get(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }




}

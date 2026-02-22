package com.insider.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// config.properties dosyasindan konfigurasyonlari okur.
// System property varsa (-Dbrowser=firefox gibi) onceligi o alir.
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("base.url");
    }

    public static String getCareersUrl() {
        return get("careers.url");
    }

    public static String getQACareersUrl() {
        return get("qa.careers.url");
    }

    public static String getApiBaseUrl() {
        return get("api.base.url");
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(get("implicit.wait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
    }
}

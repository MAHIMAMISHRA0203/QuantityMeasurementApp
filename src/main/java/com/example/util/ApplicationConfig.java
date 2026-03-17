package com.example.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Centralized configuration management for Quantity Measurement Application.
 * Loads properties from classpath application.properties file with support
 * for system property overrides and default fallback values.
 */
public class ApplicationConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private static final String CONFIG_FILE = "application.properties";
    private static ApplicationConfig instance;
    private Properties properties;
    
    private ApplicationConfig() {
        loadProperties();
    }
    
    /**
     * Get singleton instance of ApplicationConfig.
     */
    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }
    
    /**
     * Load properties from classpath application.properties file.
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.warn("Configuration file {} not found in classpath, using defaults", CONFIG_FILE);
                setDefaultProperties();
                return;
            }
            properties.load(input);
            logger.info("Configuration loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            logger.error("Error loading configuration from {}: {}", CONFIG_FILE, e.getMessage());
            setDefaultProperties();
        }
    }
    
    /**
     * Set default configuration values.
     */
    private void setDefaultProperties() {
        properties.setProperty("app.repository.type", "cache");
        properties.setProperty("db.driver", "org.h2.Driver");
        properties.setProperty("db.url", "jdbc:h2:mem:quantitydb");
        properties.setProperty("db.username", "sa");
        properties.setProperty("db.password", "");
        properties.setProperty("db.pool.minSize", "5");
        properties.setProperty("db.pool.maxSize", "20");
        properties.setProperty("db.pool.connectionTimeoutMs", "5000");
        properties.setProperty("db.pool.idleTimeoutMs", "600000");
        properties.setProperty("logging.level", "INFO");
    }
    
    /**
     * Get property value with system property override support.
     * Priority: System property > application.properties > default value
     */
    public String getProperty(String key, String defaultValue) {
        // Check system property first
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get property value without default.
     */
    public String getProperty(String key) {
        return getProperty(key, null);
    }
    
    /**
     * Get integer property value.
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property {}: {}", key, value);
            return defaultValue;
        }
    }
    
    /**
     * Get repository type (cache or database).
     */
    public String getRepositoryType() {
        return getProperty("app.repository.type", "cache");
    }
    
    /**
     * Get database driver class name.
     */
    public String getDbDriver() {
        return getProperty("db.driver", "org.h2.Driver");
    }
    
    /**
     * Get database connection URL.
     */
    public String getDbUrl() {
        return getProperty("db.url", "jdbc:h2:mem:quantitydb");
    }
    
    /**
     * Get database username.
     */
    public String getDbUsername() {
        return getProperty("db.username", "sa");
    }
    
    /**
     * Get database password.
     */
    public String getDbPassword() {
        return getProperty("db.password", "");
    }
    
    /**
     * Get minimum connection pool size.
     */
    public int getMinPoolSize() {
        return getIntProperty("db.pool.minSize", 5);
    }
    
    /**
     * Get maximum connection pool size.
     */
    public int getMaxPoolSize() {
        return getIntProperty("db.pool.maxSize", 20);
    }
    
    /**
     * Get connection timeout in milliseconds.
     */
    public long getConnectionTimeoutMs() {
        return getIntProperty("db.pool.connectionTimeoutMs", 5000);
    }
    
    /**
     * Get idle timeout in milliseconds.
     */
    public long getIdleTimeoutMs() {
        return getIntProperty("db.pool.idleTimeoutMs", 600000);
    }
}
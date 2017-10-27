package org.avirtuos.teslog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.DoubleSummaryStatistics;
import java.util.Properties;

public class PropertyConfigService implements ConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyConfigService.class);
    private final Properties properties;

    PropertyConfigService(String fileName) {
        this.properties = loadProperties(fileName);
    }

    private Properties loadProperties(String fileName){
        try {
            Properties loadProps = new Properties();
            loadProps.load(findPropertiesFile(fileName));
            return loadProps;
        } catch (IOException | RuntimeException ex){
            throw new RuntimeException(ex);
        }
    }

    private InputStream findPropertiesFile(String fileName) {
        try {
            LOGGER.info("findPropertiesFile: Searching class path for {}", fileName);
            InputStream fromClassPath = this.getClass().getClassLoader().getResourceAsStream(fileName);

            if (fromClassPath != null) {
                LOGGER.info("findPropertiesFile: Found {} on class path for {} properties", fromClassPath, fileName);
                return fromClassPath;
            }

            LOGGER.info("findPropertiesFile: Searching filesystem for {}", fileName);
            InputStream fromFileSystem = new FileInputStream(fileName);

            LOGGER.info("findPropertiesFile: Found {} on class path for {} properties", fromFileSystem, fileName);
            return fromFileSystem;

        } catch (IOException | RuntimeException ex) {
            LOGGER.error("Unable to find configuration file " + fileName + " on class path or local filesystem.");
            throw new RuntimeException("Unable to find configuration file " + fileName + " on class path or local filesystem.", ex);
        }
    }

    @Override
    public <T> T get(ConfigKey<T> configKey) {
        String returnValue = (String) properties.get(configKey.getName());

        if(returnValue == null){
            return configKey.getDefaultValue();
        }

        if(configKey.getType() == Double.class){
            return (T) (Double) Double.parseDouble(returnValue);
        } else  if(configKey.getType() == String.class){
            return (T) (String) returnValue;
        } if(configKey.getType() == Integer.class){
            return (T) (Integer) Integer.parseInt(returnValue);
        } if(configKey.getType() == Boolean.class){
            return (T) (Boolean) Boolean.parseBoolean(returnValue);
        }

        throw new RuntimeException("Unsupported type[" + configKey.getType() + "] exception for config key[" + configKey.getName()+ "]");
    }
}

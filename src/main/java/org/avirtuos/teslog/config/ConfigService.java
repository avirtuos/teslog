package org.avirtuos.teslog.config;

public interface ConfigService {
    <T> T get(ConfigKey<T> configKey);
}

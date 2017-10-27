package org.avirtuos.teslog.config;


public interface ConfigKey<T> {
    String getName();
    T getDefaultValue();
    Class<T> getType();
}

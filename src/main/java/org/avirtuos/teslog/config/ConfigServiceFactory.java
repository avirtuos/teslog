package org.avirtuos.teslog.config;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigServiceFactory {
    private static final AtomicReference<ConfigService> configService = new AtomicReference<>();

    public static synchronized void loadConfig(String fileName){
        if(configService.get() == null){
            configService.set(new PropertyConfigService(fileName));
        }
    }

    static synchronized void reset(){
        configService.set(null);
    }

    public static ConfigService get(){
        if(configService == null){
            throw new RuntimeException("ConfigService has not yet been initialized.");
        }

        return configService.get();
    }
}

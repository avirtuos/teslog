package org.avirtuos.teslog.config;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class PropertyConfigServiceTest extends TestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyConfigServiceTest.class);

    private String testConfigFile = "unit-test.properties";

    @Test
    public void testGet() throws Exception {
        LOGGER.info("testGet: enter");
        ConfigServiceFactory.loadConfig(testConfigFile);

        String actualString = ConfigServiceFactory.get().get(stringConfigKey);
        assertEquals("my string value", actualString);

        double actualDouble = ConfigServiceFactory.get().get(doubleConfigKey);
        assertEquals(2.4, actualDouble);

        int actualInt = ConfigServiceFactory.get().get(integerConfigKey);
        assertEquals(100, actualInt);

        boolean actualBool = ConfigServiceFactory.get().get(boolConfigKey);
        assertEquals(false, actualBool);

        LOGGER.info("testGet: exit");
    }


    private ConfigKey<Integer> integerConfigKey = new ConfigKey<Integer>() {
        @Override
        public String getName() {
            return "intValue";
        }

        @Override
        public Integer getDefaultValue() {
            return -1;
        }

        @Override
        public Class<Integer> getType() {
            return Integer.class;
        }
    };

    private ConfigKey<String> stringConfigKey = new ConfigKey<String>() {
        @Override
        public String getName() {
            return "stringValue";
        }

        @Override
        public String getDefaultValue() {
            return "default";
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    };

    private ConfigKey<Double> doubleConfigKey = new ConfigKey<Double>() {
        @Override
        public String getName() {
            return "doubleValue";
        }

        @Override
        public Double getDefaultValue() {
            return 1.1;
        }

        @Override
        public Class<Double> getType() {
            return Double.class;
        }
    };

    private ConfigKey<Boolean> boolConfigKey = new ConfigKey<Boolean>() {
        @Override
        public String getName() {
            return "boolValue";
        }

        @Override
        public Boolean getDefaultValue() {
            return true;
        }

        @Override
        public Class<Boolean> getType() {
            return Boolean.class;
        }
    };
}
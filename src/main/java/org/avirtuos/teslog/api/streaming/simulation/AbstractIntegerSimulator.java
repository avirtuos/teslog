package org.avirtuos.teslog.api.streaming.simulation;

import org.avirtuos.teslog.api.domain.TelemetryMessage;

import java.util.Random;

/**
 * Used to generate an oscillating integer value using an upper and lower bound as well as a scaled random
 * acceleration.
 */
public abstract class AbstractIntegerSimulator implements TelemetryMessageSimulator {
    private final int minValue;
    private final int maxValue;
    private final int accelerationFactor;
    private final Random random = new Random();
    private volatile int acceleration;
    private volatile int value;

    public AbstractIntegerSimulator(int minValue, int maxValue, int accelerationFactor) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.accelerationFactor = accelerationFactor;
    }

    public AbstractIntegerSimulator(int initialValue, int minValue, int maxValue, int accelerationFactor) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = initialValue;
        this.accelerationFactor = accelerationFactor;
    }

    @Override
    public TelemetryMessage enrich(TelemetryMessage message) {
        int effectiveAcceleration = getOrUpdateAcceleration();
        value += effectiveAcceleration;
        return doEnrich(acceleration, value, message);
    }

    protected abstract TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message);

    protected int getOrUpdateAcceleration(){
        if(value >= maxValue){
            acceleration = -1 * (1 + random.nextInt(maxValue/accelerationFactor));
        } else if (value <= minValue){
            acceleration = (1 + random.nextInt(maxValue/accelerationFactor));
        }

        return acceleration;
    }

}

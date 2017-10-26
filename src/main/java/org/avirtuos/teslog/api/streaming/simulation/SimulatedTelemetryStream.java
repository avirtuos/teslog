package org.avirtuos.teslog.api.streaming.simulation;


import org.avirtuos.teslog.api.domain.TelemetryMessage;
import org.avirtuos.teslog.api.streaming.TelemetryListener;
import org.avirtuos.teslog.api.streaming.TelemetryStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulatedTelemetryStream implements TelemetryStream {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedTelemetryStream.class);

    private final int msgIntervalSeconds;
    private final List<TelemetryMessageSimulator> simulatorChain = new ArrayList<>();
    private volatile ScheduledExecutorService executorService = null;
    private final Map<String, TelemetryListener> subscriptions = new ConcurrentHashMap<>();

    public SimulatedTelemetryStream(int msgIntervalSeconds) {
        this.msgIntervalSeconds = msgIntervalSeconds;

        //speed and power simulator
        simulatorChain.add(new AbstractIntegerSimulator(0, 100, 30) {
            @Override
            protected TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message) {
                return message.newBuilder(message).withSpeed(value).withPower(acceleration * 30).build();
            }
        });

        //odometer simulator
        simulatorChain.add(new AbstractIntegerSimulator(0, 100_000, 100_000) {
            @Override
            protected TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message) {
                return message.newBuilder(message).withOdometerReading(value + Math.abs(acceleration)).build();
            }
        });

        //battery percent simulator
        simulatorChain.add(new AbstractIntegerSimulator(0, 100, 100) {
            @Override
            protected TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message) {
                return message.newBuilder(message).withBatteryPercent(value).build();
            }
        });

        //elevation simulator
        simulatorChain.add(new AbstractIntegerSimulator(-100, 1000, 10) {
            @Override
            protected TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message) {
                return message.newBuilder(message).withElevation(value).build();
            }
        });

        //range simulator
        simulatorChain.add(new AbstractIntegerSimulator(300, 0, 300, 30) {
            @Override
            protected TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message) {
                return message.newBuilder(message).withRatedRange(value).withEstRange(value).build();
            }
        });

        //range simulator
        simulatorChain.add(new TelemetryMessageSimulator() {
            @Override
            public TelemetryMessage enrich(TelemetryMessage message) {
                return message.newBuilder(message).withShiftState("drive").build();
            }
        });

        //position simulator
        simulatorChain.add(new AbstractIntegerSimulator(1500, 0, 3000, 1000) {
            @Override
            protected TelemetryMessage doEnrich(int acceleration, int value, TelemetryMessage message) {
                return message.newBuilder(message)
                        .withEstHeading(acceleration)
                        .withHeading(acceleration)
                        .withLatitude(value)
                        .withLongitude(value + acceleration)
                        .withEstRange(value)
                        .build();
            }
        });
    }


    @Override
    public void subscribe(TelemetryListener listener) {
        if(subscriptions.putIfAbsent(listener.getId(), listener) != null){
            throw new RuntimeException("Subscription with id " + listener.getId() + " already exists.");
        }
    }

    @Override
    public void unsubscribe(TelemetryListener listener) {
        if(!subscriptions.containsKey(listener.getId())){
            throw new RuntimeException("No subscription exists for id " + listener.getId());
        }

        subscriptions.remove(listener.getId());
    }

    @Override
    public void start() {
        if(executorService != null){
            throw new RuntimeException("Already running, call stop first in order to start again.");
        }

        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(new MessageGenerator(), 0, msgIntervalSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        executorService.shutdown();
        executorService = null;
    }

    private class MessageGenerator implements Runnable {
        @Override
        public void run() {
            TelemetryMessage msg = TelemetryMessage.newBuilder().withSampleTime(System.currentTimeMillis()).build();
            for(TelemetryMessageSimulator nextSimulator : simulatorChain){
                msg = nextSimulator.enrich(msg);
            }

            for(TelemetryListener nextSubscription : subscriptions.values()){
                try{
                    nextSubscription.onMessage(msg);
                } catch (RuntimeException ex) {
                    LOGGER.error("Unexpected exception while muxing message to {}", nextSubscription.getId(), ex);
                }
            }
        }
    }

}

package org.avirtuos.teslog.prototype;

import org.avirtuos.teslog.api.streaming.TelemetryStream;
import org.avirtuos.teslog.api.streaming.simulation.SimulatedTelemetryStream;
import org.avirtuos.teslog.rrd.RrdTelemetryStore;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        TelemetryStream stream = new SimulatedTelemetryStream(1);
        RrdTelemetryStore telemetryStore = new RrdTelemetryStore();
        stream.subscribe(telemetryStore);
        stream.start();

        for(int i = 0; i < 500; i++){
            Thread.sleep(1000);
            if(i > 1 && i % 60 == 0) {
                telemetryStore.render();
            }
        }
    }
}

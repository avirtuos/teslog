package org.avirtuos.teslog.rrd;


import org.avirtuos.teslog.api.domain.TelemetryMessage;
import org.avirtuos.teslog.api.streaming.TelemetryListener;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.data.Variable;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

import static org.rrd4j.ConsolFun.*;
import static org.rrd4j.DsType.ABSOLUTE;
import static org.rrd4j.DsType.GAUGE;

public class RrdTelemetryStore implements TelemetryListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RrdTelemetryStore.class);

    private static final String LISTENER_ID = "RRD_TELEMETRY_STORE";
    private final RrdDef rrdDef;
    private volatile RrdDb rrdDb;

    public RrdTelemetryStore() {
        try {
            rrdDef = new RrdDef("/tmp/test.rrd", 1);

            rrdDef.addDatasource("speed", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("odometer", ABSOLUTE, 60, 0, Double.NaN);
            rrdDef.addDatasource("batteryPercent", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("elevation", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("estHeading", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("heading", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("latitude", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("longitude", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("power", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("ratedRange", GAUGE, 60, 0, Double.NaN);
            rrdDef.addDatasource("estRange", GAUGE, 60, 0, Double.NaN);

            //14 Days @ 1 Second
            rrdDef.addArchive(AVERAGE, 0.5, 1, 20_000);

            //90 Days @ 1 Minutes
            rrdDef.addArchive(AVERAGE, 0.5, 60, 130_000);
            rrdDef.addArchive(MAX, 0.5, 60, 130_000);

            //Ten Years @ 1 Hour
            rrdDef.addArchive(AVERAGE, 0.5, 3600, 88_000);
            rrdDef.addArchive(MAX, 0.5, 3600, 88_000);

            rrdDb = new RrdDb(rrdDef);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getId() {
        return LISTENER_ID;
    }

    @Override
    public synchronized void onMessage(TelemetryMessage message) {
        try {
            LOGGER.info("onMessage: message[{}]", message);

            Sample sample = rrdDb.createSample();
            sample.setTime(message.getSampleTime());
            sample.setValue("speed", message.getSpeed());
            sample.setValue("odometer", message.getOdometerReading());
            sample.setValue("batteryPercent", message.getBatteryPercent());
            sample.setValue("elevation", message.getElevation());
            sample.setValue("estHeading", message.getEstHeading());
            sample.setValue("heading", message.getHeading());
            sample.setValue("latitude", message.getLatitude());
            sample.setValue("longitude", message.getLongitude());
            sample.setValue("power", message.getPower());
            sample.setValue("ratedRange", message.getRatedRange());
            sample.setValue("estRange", message.getEstRange());
            sample.update();
        } catch (IOException ex) {
            LOGGER.error("onMessage: Unable to process message.", ex);
        }
    }

    private synchronized void flushRdd() {
        try {
            LOGGER.info("flushRdd: Flushing RRD.");
            rrdDb.close();
            rrdDb = new RrdDb(rrdDef);
            LOGGER.info("flushRdd: Flushed RRD.");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void render() {
        try {
            flushRdd();

// then create a graph definition
            RrdGraphDef gDef = new RrdGraphDef();
            gDef.setWidth(800);
            gDef.setHeight(600);
            gDef.setFilename("/tmp/sample.png");
            gDef.setStartTime((System.currentTimeMillis()/1000) - (500));
            gDef.setEndTime(System.currentTimeMillis()/1000);
            gDef.setTitle("My Title");
            gDef.setVerticalLabel("bytes");

            gDef.datasource("speed", "/tmp/test.rrd", "speed", AVERAGE);
            gDef.datasource("batteryPercent", "/tmp/test.rrd", "batteryPercent", AVERAGE);
            gDef.datasource("power", "/tmp/test.rrd", "power", AVERAGE);
            gDef.datasource("estRange", "/tmp/test.rrd", "estRange", AVERAGE);

            gDef.line("speed", Color.GREEN, "speed mph");
            gDef.line("batteryPercent", Color.MAGENTA, "batteryPercent");
            gDef.area("power", Color.YELLOW, "power");
            gDef.line("estRange", Color.RED, null);

            gDef.comment("\\r");


            Variable speedmax = new Variable.MAX();
            Variable powermax = new Variable.MAX();
            gDef.datasource("speedmax", "speed", speedmax);
            gDef.datasource("powermax", "power", powermax);
            gDef.gprint("speedmax", "speedmax = %.3f%s");
            gDef.gprint("powermax", "powermax = %.3f%S\\c");

            gDef.setImageFormat("png");

            RrdGraph graph = new RrdGraph(gDef);

            LOGGER.info("render: graphInfo[{}]", graph.getRrdGraphInfo().dump());
        } catch (IOException ex) {
            LOGGER.error("render: Error rendering graph.", ex);
        }
    }
}

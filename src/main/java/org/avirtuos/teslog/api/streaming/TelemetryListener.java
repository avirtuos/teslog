package org.avirtuos.teslog.api.streaming;

import org.avirtuos.teslog.api.domain.TelemetryMessage;

/**
 * Classes wishing to consume streaming telemetry data should implement this interface.
 */
public interface TelemetryListener {
    /**
     * Used to obtain the id of the TelemetryListener.
     * @return The unique identifier for the instance of a TelemetryListener.
     */
    String getId();

    /**
     * Used to process a newly arrived TelemetryMessage.
     * @param message The newly arrived TelemetryMessage.
     */
    void onMessage(TelemetryMessage message);
}

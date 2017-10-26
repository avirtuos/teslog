package org.avirtuos.teslog.api.streaming.simulation;

import org.avirtuos.teslog.api.domain.TelemetryMessage;

/**
 * Defines a message simulator that can be chained.
 */
public interface TelemetryMessageSimulator {

    /**
     * Used to enrich a TelemetryMessage.
     * @param message The message to enrich.
     * @return The enriched message.
     */
    TelemetryMessage enrich(TelemetryMessage message);
}

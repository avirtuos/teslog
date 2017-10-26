package org.avirtuos.teslog.api.streaming.simulation;

import junit.framework.TestCase;
import org.avirtuos.teslog.api.domain.TelemetryMessage;
import org.avirtuos.teslog.api.streaming.TelemetryListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class SimulatedTelemetryStreamTest extends TestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedTelemetryStreamTest.class);

    private int msgIntervalSeconds = 1;
    private SimulatedTelemetryStream stream = new SimulatedTelemetryStream(msgIntervalSeconds);

    @Mock
    private TelemetryListener mockListener;

    @Test
    public void testSimulation() throws Exception {
        reset(mockListener);
        when(mockListener.getId()).thenReturn("id");
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                TelemetryMessage msg = invocationOnMock.getArgumentAt(0, TelemetryMessage.class);
                LOGGER.info("Recieved msg[{}]", msg);
                return null;
            }
        }).when(mockListener).onMessage(any(TelemetryMessage.class));
        stream.subscribe(mockListener);
        stream.start();
        Thread.sleep(msgIntervalSeconds * 4 * 1000);
        stream.stop();
        verify(mockListener, atLeastOnce()).getId();
        verify(mockListener, atLeast(2)).onMessage(any(TelemetryMessage.class));
    }

}
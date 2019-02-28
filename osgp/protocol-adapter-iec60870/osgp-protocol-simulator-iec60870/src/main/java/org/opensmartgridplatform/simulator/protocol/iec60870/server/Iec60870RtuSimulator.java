package org.opensmartgridplatform.simulator.protocol.iec60870.server;

import java.io.IOException;

import org.openmuc.j60870.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Iec60870RtuSimulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iec60870RtuSimulator.class);

    private Iec60870ServerEventListener iec60870ServerEventListener;

    public Iec60870RtuSimulator(final Iec60870ServerEventListener iec60870ServerEventListener) {
        this.iec60870ServerEventListener = iec60870ServerEventListener;
    }

    public void start() {
        final Server server = new Server.Builder().build();

        try {
            LOGGER.info("Starting IEC60870 Server.");
            server.start(this.iec60870ServerEventListener);
        } catch (final IOException e) {
            LOGGER.error("Exception occurred while starting IEC60870 server: {}.", e.getMessage());
        }
    }

}

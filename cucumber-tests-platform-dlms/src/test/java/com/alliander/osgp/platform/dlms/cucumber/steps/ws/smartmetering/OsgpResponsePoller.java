/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.platform.cucumber.support.ws.WebServiceSecurityException;

public abstract class OsgpResponsePoller {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsgpResponsePoller.class);

    private final int maxTimeResponseAvailability;
    private final int sleepTime = 100;

    public OsgpResponsePoller(final int maxTimeResponseAvailability) {
        this.maxTimeResponseAvailability = maxTimeResponseAvailability;
    }

    abstract protected Object polWsResponse() throws WebServiceSecurityException;

    /**
     * Polls OSGP for response availability for a max time.
     *
     * @return the actual ws response object
     * @throws InterruptedException
     *             if interrupted during sleep
     * @throws WebServiceSecurityException
     *             if an unexpected ws exception occurs
     */
    public Object startResponsePoller() throws InterruptedException, WebServiceSecurityException {

        int timeSlept = 0;

        while (timeSlept < this.maxTimeResponseAvailability) {
            try {
                final Object responseObject = this.polWsResponse();
                return responseObject;
            } catch (final SoapFaultClientException e) {
                if ("CorrelationUid is unknown.".equals(e.getMessage())) {
                    LOGGER.warn("CorrelationId is not available yet");
                } else {
                    LOGGER.error("Unexpected exception", e);
                    throw e;
                }

                Thread.sleep(this.sleepTime);
                timeSlept += this.sleepTime;
            }

        }
        throw new AssertionError("Correlation Uid is not available in time. Time slept is " + timeSlept);
    }
}

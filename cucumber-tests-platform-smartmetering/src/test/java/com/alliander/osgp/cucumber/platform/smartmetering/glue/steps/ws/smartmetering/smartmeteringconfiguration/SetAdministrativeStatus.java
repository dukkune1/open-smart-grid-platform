/**
/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.smartmeteringconfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.ws.schema.smartmetering.common.OsgpResultType;
import com.alliander.osgp.adapter.ws.schema.smartmetering.configuration.SetAdministrativeStatusAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.configuration.SetAdministrativeStatusAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.configuration.SetAdministrativeStatusRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.configuration.SetAdministrativeStatusResponse;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.smartmetering.PlatformSmartmeteringKeys;
import com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.SmartMeteringStepsBase;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.configuration.SetAdministrativeStatusRequestFactory;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.configuration.SmartMeteringConfigurationClient;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SetAdministrativeStatus extends SmartMeteringStepsBase {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SetAdministrativeStatus.class);

    @Autowired
    private SmartMeteringConfigurationClient smartMeteringConfigurationClient;

    @When("^the set administrative status request is received$")
    public void theSetAdministrativeStatusRequestIsReceived(final Map<String, String> requestData) throws Throwable {
        final Map<String, String> settings = new HashMap<>();
        settings.put(PlatformSmartmeteringKeys.KEY_DEVICE_IDENTIFICATION,
                requestData.get(PlatformSmartmeteringKeys.KEY_DEVICE_IDENTIFICATION));

        settings.put(PlatformSmartmeteringKeys.ADMINISTRATIVE_STATUS_TYPE,
                requestData.get(PlatformSmartmeteringKeys.ADMINISTRATIVE_STATUS_TYPE));

        final SetAdministrativeStatusRequest setAdministrativeStatusRequest = SetAdministrativeStatusRequestFactory
                .fromParameterMap(settings);

        final SetAdministrativeStatusAsyncResponse setAdministrativeStatusAsyncResponse = this.smartMeteringConfigurationClient
                .setAdministrativeStatus(setAdministrativeStatusRequest);

        LOGGER.info("Set administrative status response is received {}", setAdministrativeStatusAsyncResponse);

        assertNotNull("Set administrative status response should not be null", setAdministrativeStatusAsyncResponse);
        ScenarioContext.current().put(PlatformSmartmeteringKeys.KEY_CORRELATION_UID,
                setAdministrativeStatusAsyncResponse.getCorrelationUid());
    }

    @Then("^the administrative status should be set on the device$")
    public void theAdministrativeStatusShouldBeSetOnTheDevice(final Map<String, String> settings) throws Throwable {
        final SetAdministrativeStatusAsyncRequest setAdministrativeStatusAsyncRequest = SetAdministrativeStatusRequestFactory
                .fromScenarioContext();
        final SetAdministrativeStatusResponse setAdministrativeStatusResponse = this.smartMeteringConfigurationClient
                .retrieveSetAdministrativeStatusResponse(setAdministrativeStatusAsyncRequest);

        LOGGER.info("The administrative status result is: {}", setAdministrativeStatusResponse.getResult());
        assertNotNull("Administrative status type result is null", setAdministrativeStatusResponse.getResult());
        assertEquals("Administrative status type should be OK", OsgpResultType.OK,
                setAdministrativeStatusResponse.getResult());
    }
}

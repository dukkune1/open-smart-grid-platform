/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.smartmeteringmonitoring;

import static com.alliander.osgp.cucumber.core.Helpers.getDate;
import static com.alliander.osgp.cucumber.core.Helpers.getString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodType;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.monitoring.PeriodicMeterReadsGasResponse;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformDefaults;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.smartmetering.builders.PeriodicMeterReadsGasRequestBuilder;
import com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.SmartMeteringStepsBase;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.monitoring.SmartMeteringMonitoringRequestClient;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.monitoring.SmartMeteringMonitoringResponseClient;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeriodicMeterReadsGas extends SmartMeteringStepsBase {

    @Autowired
    private SmartMeteringMonitoringRequestClient<PeriodicMeterReadsGasAsyncResponse, PeriodicMeterReadsGasRequest> requestClient;

    @Autowired
    private SmartMeteringMonitoringResponseClient<PeriodicMeterReadsGasResponse, PeriodicMeterReadsGasAsyncRequest> responseClient;

    @When("^the get \"([^\"]*)\" meter reads gas request is received$")
    public void theGetMeterReadsGasRequestIsReceived(final String stringPeriodType, final Map<String, String> settings)
            throws Throwable {

        final String deviceIdentification = getString(settings, PlatformKeys.KEY_DEVICE_IDENTIFICATION,
                PlatformDefaults.DEFAULT_DEVICE_IDENTIFICATION);
        final PeriodType periodType = PeriodType.fromValue(stringPeriodType);
        final DateTime beginDate = this.fillDate(settings, PlatformKeys.KEY_BEGIN_DATE);
        final DateTime endDate = this.fillDate(settings, PlatformKeys.KEY_END_DATE);

        ScenarioContext.current().put(PlatformKeys.KEY_DEVICE_IDENTIFICATION, deviceIdentification);
        ScenarioContext.current().put(PlatformKeys.KEY_BEGIN_DATE, beginDate);
        ScenarioContext.current().put(PlatformKeys.KEY_END_DATE, endDate);

        final PeriodicMeterReadsGasRequest request = new PeriodicMeterReadsGasRequestBuilder()
                .withDeviceIdentification(deviceIdentification).withPeriodType(periodType).withBeginDate(beginDate)
                .withEndDate(endDate).build();

        final PeriodicMeterReadsGasAsyncResponse asyncResponse = this.requestClient.doRequest(request);
        assertNotNull(asyncResponse);
        ScenarioContext.current().put(PlatformKeys.KEY_CORRELATION_UID, asyncResponse.getCorrelationUid());
    }

    @Then("^the \"([^\"]*)\" meter reads gas result should be returned$")
    public void theMeterReadsGasResultShouldBeReturned(final String periodType, final Map<String, String> settings)
            throws Throwable {

        final String deviceIdentification = (String) ScenarioContext.current()
                .get(PlatformKeys.KEY_DEVICE_IDENTIFICATION);
        final String correlationUid = (String) ScenarioContext.current().get(PlatformKeys.KEY_CORRELATION_UID);

        final PeriodicMeterReadsGasAsyncRequest asyncRequest = new PeriodicMeterReadsGasAsyncRequest();
        asyncRequest.setCorrelationUid(correlationUid);
        asyncRequest.setDeviceIdentification(deviceIdentification);

        final PeriodicMeterReadsGasResponse response = this.responseClient.getResponse(asyncRequest, correlationUid);

        assertNotNull("PeriodicMeterReadsGasResponse should not be null", response);
        assertEquals("PeriodType should match", PeriodType.fromValue(periodType), response.getPeriodType());
        assertNotNull("Expected periodic meter reads gas", response.getPeriodicMeterReadsGas());
    }

    private DateTime fillDate(final Map<String, String> settings, final String key) {
        return getDate(settings, key, new DateTime());
    }

}

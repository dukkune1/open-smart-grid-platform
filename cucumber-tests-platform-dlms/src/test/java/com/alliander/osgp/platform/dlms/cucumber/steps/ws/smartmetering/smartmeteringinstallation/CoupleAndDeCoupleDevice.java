/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering.smartmeteringinstallation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.adapter.ws.schema.smartmetering.common.OsgpResultType;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.CoupleMbusDeviceResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.DeCoupleMbusDeviceAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.DeCoupleMbusDeviceAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.DeCoupleMbusDeviceRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.DeCoupleMbusDeviceResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.installation.ObjectFactory;
import com.alliander.osgp.platform.cucumber.core.ScenarioContext;
import com.alliander.osgp.platform.cucumber.steps.Keys;
import com.alliander.osgp.platform.cucumber.steps.ws.OsgpResponsePoller;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceSecurityException;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceTemplateFactory;
import com.alliander.osgp.platform.dlms.cucumber.steps.ws.smartmetering.AbstractSmartMeteringSteps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CoupleAndDeCoupleDevice extends AbstractSmartMeteringSteps {

    protected static final Map<String, String> PROPERTIES_MAP = new HashMap<>();

    @Value("${osgp.response.poller.maxWaitTimeForResponse}")
    private int maxWaitTimeForResponse;

    @Value("${osgp.response.poller.sleepTime}")
    private int sleepTime;

    @Autowired
    private WebServiceTemplateFactory smartMeteringInstallationManagementWstf;

    @When("^the Couple G-meter \"([^\"]*)\" request on channel (\\d+) is received$")
    public void theCoupleGMeterRequestIsReceived(final String gasMeter, final Short channel)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {

        final CoupleMbusDeviceRequest request = new ObjectFactory().createCoupleMbusDeviceRequest();
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));
        request.setMbusDeviceIdentification(gasMeter);
        request.setChannel(channel);

        final CoupleMbusDeviceAsyncResponse response = (CoupleMbusDeviceAsyncResponse) this.smartMeteringInstallationManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName()).marshalSendAndReceive(request);

        this.checkAndSaveCorrelationId(response.getCorrelationUid());
    }

    @When("^the Couple G-meter \"([^\"]*)\" to E-meter \"([^\"]*)\" request on channel (\\d+) is received$")
    public void theCoupleGMeterToEMeterRequestOnChannelIsReceived(final String gasMeter, final String eMeter,
            final Short channel) throws WebServiceSecurityException, GeneralSecurityException, IOException {

        final CoupleMbusDeviceRequest request = new ObjectFactory().createCoupleMbusDeviceRequest();
        request.setDeviceIdentification(eMeter);
        request.setMbusDeviceIdentification(gasMeter);
        request.setChannel(channel);

        final CoupleMbusDeviceResponse response = (CoupleMbusDeviceResponse) this.smartMeteringInstallationManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName()).marshalSendAndReceive(request);

        final OsgpResultType result = response.getResult();
        assertTrue(OsgpResultType.OK.equals(result));
    }

    @Then("^the Couple G-meter \"([^\"]*)\" to E-meter \"([^\"]*)\" request on channel (\\d+) throws an SoapException with message \"([^\"]*)\"$")
    public void theCoupleGMeterToEMeterRequestOnChannelThrowsAnSoapException(final String gasMeter,
            final String eMeter, final Short channel, final String soapExceptionMessage)
                    throws WebServiceSecurityException, GeneralSecurityException, IOException {
        final CoupleMbusDeviceRequest request = new ObjectFactory().createCoupleMbusDeviceRequest();
        request.setDeviceIdentification(eMeter);
        request.setMbusDeviceIdentification(gasMeter);
        request.setChannel(channel);

        try {
            this.smartMeteringInstallationManagementWstf.getTemplate(this.getOrganizationIdentification(),
                    this.getUserName()).marshalSendAndReceive(request);
            fail("A SoapFaultClientException should be thrown");
        } catch (final SoapFaultClientException e) {
            assertEquals(soapExceptionMessage, e.getMessage());
        }

    }

    @Then("^the DeCouple G-meter \"([^\"]*)\" from E-meter \"([^\"]*)\" request throws an SoapException with message \"([^\"]*)\"$")
    public void theDeCoupleGMeterToEMeterRequestThrowsAnSoapException(final String gasMeter, final String eMeter,
            final String soapExceptionMessage) throws WebServiceSecurityException, GeneralSecurityException,
            IOException {
        final DeCoupleMbusDeviceRequest request = new ObjectFactory().createDeCoupleMbusDeviceRequest();
        request.setDeviceIdentification(eMeter);
        request.setMbusDeviceIdentification(gasMeter);

        try {
            this.smartMeteringInstallationManagementWstf.getTemplate(this.getOrganizationIdentification(),
                    this.getUserName()).marshalSendAndReceive(request);
            fail("A SoapFaultClientException should be thrown");
        } catch (final SoapFaultClientException e) {
            assertEquals(soapExceptionMessage, e.getMessage());
        }

    }

    @When("^the Couple G-meter \"([^\"]*)\" request on channel (\\d+) is throws an SoapException with message \"([^\"]*)\"$")
    public void theCoupleGMeterRequestOnChannelIsThrowsAnSoapExceptionWithMessage(final String gasMeter,
            final Short channel, final String soapExceptionMessage) throws WebServiceSecurityException,
            GeneralSecurityException, IOException {
        final CoupleMbusDeviceRequest request = new ObjectFactory().createCoupleMbusDeviceRequest();
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));
        request.setMbusDeviceIdentification(gasMeter);
        request.setChannel(channel);

        try {
            this.smartMeteringInstallationManagementWstf.getTemplate(this.getOrganizationIdentification(),
                    this.getUserName()).marshalSendAndReceive(request);
            fail("A SoapFaultClientException should be thrown");
        } catch (final SoapFaultClientException e) {
            assertEquals(soapExceptionMessage, e.getMessage());
        }

    }

    @When("^the DeCouple G-meter \"([^\"]*)\" request is received$")
    public void theDecoupleGMeterRequestIsReceived(final String gasMeter) throws WebServiceSecurityException,
            GeneralSecurityException, IOException {

        final DeCoupleMbusDeviceRequest request = new ObjectFactory().createDeCoupleMbusDeviceRequest();
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));
        request.setMbusDeviceIdentification(gasMeter);
        final DeCoupleMbusDeviceAsyncResponse response = (DeCoupleMbusDeviceAsyncResponse) this.smartMeteringInstallationManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName()).marshalSendAndReceive(request);

        this.checkAndSaveCorrelationId(response.getCorrelationUid());
    }

    @When("^the DeCouple G-meter \"([^\"]*)\" from E-meter \"([^\"]*)\" request is received$")
    public void theDeCoupleGMeterFromEMeterRequestIsReceived(final String gasMeter, final String eMeter)
            throws WebServiceSecurityException, GeneralSecurityException, IOException {

        final DeCoupleMbusDeviceRequest request = new ObjectFactory().createDeCoupleMbusDeviceRequest();
        request.setDeviceIdentification(eMeter);
        request.setMbusDeviceIdentification(gasMeter);
        final DeCoupleMbusDeviceAsyncResponse response = (DeCoupleMbusDeviceAsyncResponse) this.smartMeteringInstallationManagementWstf
                .getTemplate(this.getOrganizationIdentification(), this.getUserName()).marshalSendAndReceive(request);

        this.checkAndSaveCorrelationId(response.getCorrelationUid());
    }

    @Then("^the couple response is \"([^\"]*)\"$")
    public void theCoupleResponseIs(final String status) throws WebServiceSecurityException, InterruptedException,
            GeneralSecurityException, IOException {

        final CoupleMbusDeviceAsyncRequest request = new ObjectFactory().createCoupleMbusDeviceAsyncRequest();
        request.setCorrelationUid((String) ScenarioContext.Current().get(Keys.KEY_CORRELATION_UID));
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));

        final OsgpResponsePoller<Object> responsePoller = this.createCoupleMbusResponsePoller(request);
        final OsgpResultType resultType = OsgpResultType.fromValue(status);

        final CoupleMbusDeviceResponse response = (CoupleMbusDeviceResponse) responsePoller.start();

        assertTrue(resultType.equals(response.getResult()));

    }

    @Then("^the couple response is \"([^\"]*)\" and contains$")
    public void theCoupleResponseContains(final String status, final List<String> resultList)
            throws WebServiceSecurityException, InterruptedException, GeneralSecurityException, IOException {

        final CoupleMbusDeviceAsyncRequest request = new ObjectFactory().createCoupleMbusDeviceAsyncRequest();
        request.setCorrelationUid((String) ScenarioContext.Current().get(Keys.KEY_CORRELATION_UID));
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));

        final OsgpResponsePoller<Object> responsePoller = this.createCoupleMbusResponsePoller(request);
        final OsgpResultType resultType = OsgpResultType.fromValue(status);

        final CoupleMbusDeviceResponse response = (CoupleMbusDeviceResponse) responsePoller.start();

        assertTrue(resultType.equals(response.getResult()));

        assertTrue(this.checkDescription(response.getDescription(), resultList));
    }

    @Then("^the DeCouple response is \"([^\"]*)\" and contains$")
    public void theDeCoupleResponseIsAndContains(final String status, final List<String> resultList)
            throws WebServiceSecurityException, InterruptedException, GeneralSecurityException, IOException {

        final DeCoupleMbusDeviceAsyncRequest request = new ObjectFactory().createDeCoupleMbusDeviceAsyncRequest();
        request.setCorrelationUid((String) ScenarioContext.Current().get(Keys.KEY_CORRELATION_UID));
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));

        final OsgpResponsePoller<Object> responsePoller = this.createDeCoupleResponsePoller(request);
        final DeCoupleMbusDeviceResponse response = (DeCoupleMbusDeviceResponse) responsePoller.start();

        final OsgpResultType resultType = OsgpResultType.fromValue(status);

        assertTrue(resultType.equals(response.getResult()));
        assertTrue(this.checkDescription(response.getDescription(), resultList));

    }

    @Then("^the DeCouple response is \"([^\"]*)\"$")
    public void theDeCoupleResponseIsAndContains(final String status) throws WebServiceSecurityException,
            InterruptedException, GeneralSecurityException, IOException {

        final DeCoupleMbusDeviceAsyncRequest request = new ObjectFactory().createDeCoupleMbusDeviceAsyncRequest();
        request.setCorrelationUid((String) ScenarioContext.Current().get(Keys.KEY_CORRELATION_UID));
        request.setDeviceIdentification((String) ScenarioContext.Current().get(Keys.KEY_DEVICE_IDENTIFICATION));

        final OsgpResponsePoller<Object> responsePoller = this.createDeCoupleResponsePoller(request);

        final DeCoupleMbusDeviceResponse response = (DeCoupleMbusDeviceResponse) responsePoller.start();

        final OsgpResultType resultType = OsgpResultType.fromValue(status);

        assertTrue(resultType.equals(response.getResult()));

    }

    private OsgpResponsePoller<Object> createDeCoupleResponsePoller(final DeCoupleMbusDeviceAsyncRequest request) {
        final OsgpResponsePoller<Object> responsePoller = new OsgpResponsePoller<Object>(this.maxWaitTimeForResponse,
                this.sleepTime) {

            @Override
            public Object pollWsResponse() throws WebServiceSecurityException, GeneralSecurityException, IOException {
                final DeCoupleMbusDeviceResponse response = (DeCoupleMbusDeviceResponse) CoupleAndDeCoupleDevice.this.smartMeteringInstallationManagementWstf
                        .getTemplate(CoupleAndDeCoupleDevice.this.getOrganizationIdentification(),
                                CoupleAndDeCoupleDevice.this.getUserName()).marshalSendAndReceive(request);

                response.getDescription();

                return response;
            }
        };
        return responsePoller;
    }

    private OsgpResponsePoller<Object> createCoupleMbusResponsePoller(final CoupleMbusDeviceAsyncRequest request) {
        final OsgpResponsePoller<Object> responsePoller = new OsgpResponsePoller<Object>(this.maxWaitTimeForResponse,
                this.sleepTime) {

            @Override
            public Object pollWsResponse() throws WebServiceSecurityException, GeneralSecurityException, IOException {

                final CoupleMbusDeviceResponse response = (CoupleMbusDeviceResponse) CoupleAndDeCoupleDevice.this.smartMeteringInstallationManagementWstf
                        .getTemplate(CoupleAndDeCoupleDevice.this.getOrganizationIdentification(),
                                CoupleAndDeCoupleDevice.this.getUserName()).marshalSendAndReceive(request);
                return response;
            }
        };
        return responsePoller;
    }

}

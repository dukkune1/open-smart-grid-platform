/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.support.ws.core.deviceinstallation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.AddDeviceRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.AddDeviceResponse;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.FindRecentDevicesRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.FindRecentDevicesResponse;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StartDeviceTestAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StartDeviceTestAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StartDeviceTestRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StartDeviceTestResponse;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StopDeviceTestAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StopDeviceTestAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StopDeviceTestRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.StopDeviceTestResponse;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.UpdateDeviceRequest;
import com.alliander.osgp.adapter.ws.schema.core.deviceinstallation.UpdateDeviceResponse;
import com.alliander.osgp.adapter.ws.schema.core.devicemanagement.SetEventNotificationsAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.core.devicemanagement.SetEventNotificationsAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.core.devicemanagement.SetEventNotificationsRequest;
import com.alliander.osgp.adapter.ws.schema.core.devicemanagement.SetEventNotificationsResponse;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.GetFirmwareVersionAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.GetFirmwareVersionAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.GetFirmwareVersionRequest;
import com.alliander.osgp.adapter.ws.schema.core.firmwaremanagement.GetFirmwareVersionResponse;
import com.alliander.osgp.platform.cucumber.support.ws.BaseClient;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceSecurityException;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceTemplateFactory;

@Component
public class CoreDeviceInstallationClient extends BaseClient {
	
    @Autowired
    private WebServiceTemplateFactory coreDeviceInstallationWstf;

    public AddDeviceResponse addDevice(final AddDeviceRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (AddDeviceResponse) wst.marshalSendAndReceive(request);
    }

    public UpdateDeviceResponse updateDevice(final UpdateDeviceRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (UpdateDeviceResponse) wst.marshalSendAndReceive(request);
    }

    public StartDeviceTestAsyncResponse startDeviceTest(final StartDeviceTestRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (StartDeviceTestAsyncResponse) wst.marshalSendAndReceive(request);
    }

    public FindRecentDevicesResponse findRecentDevices(final FindRecentDevicesRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (FindRecentDevicesResponse) wst.marshalSendAndReceive(request);
    }

	public StartDeviceTestResponse getStartDeviceTestResponse(StartDeviceTestAsyncRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (StartDeviceTestResponse) wst.marshalSendAndReceive(request);
	}

	public StopDeviceTestAsyncResponse stopDeviceTest(StopDeviceTestRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (StopDeviceTestAsyncResponse) wst.marshalSendAndReceive(request);
	}

	public StopDeviceTestResponse getStopDeviceTestResponse(StopDeviceTestAsyncRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (StopDeviceTestResponse) wst.marshalSendAndReceive(request);
	}

	public SetEventNotificationsAsyncResponse setEventNotifications(SetEventNotificationsRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (SetEventNotificationsAsyncResponse) wst.marshalSendAndReceive(request);
	}

	public SetEventNotificationsResponse getSetEventNotificationsResponse(SetEventNotificationsAsyncRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (SetEventNotificationsResponse) wst.marshalSendAndReceive(request);
	}

	public GetFirmwareVersionAsyncResponse getFirmwareVersion(GetFirmwareVersionRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetFirmwareVersionAsyncResponse) wst.marshalSendAndReceive(request);
	}

	public GetFirmwareVersionResponse getGetFirmwareVersion(GetFirmwareVersionAsyncRequest request) throws WebServiceSecurityException {
        final WebServiceTemplate wst = this.coreDeviceInstallationWstf.getTemplate(this.getOrganizationIdentification(), this.getUserName());
        return (GetFirmwareVersionResponse) wst.marshalSendAndReceive(request);
	}    
}

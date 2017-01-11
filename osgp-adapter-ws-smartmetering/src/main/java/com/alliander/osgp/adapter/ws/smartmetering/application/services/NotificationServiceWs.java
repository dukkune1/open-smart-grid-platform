/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.ws.smartmetering.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.ws.client.WebServiceTransportException;

import com.alliander.osgp.adapter.ws.schema.smartmetering.notification.Notification;
import com.alliander.osgp.adapter.ws.schema.smartmetering.notification.NotificationType;
import com.alliander.osgp.adapter.ws.smartmetering.exceptions.WebServiceSecurityException;
import com.alliander.osgp.adapter.ws.smartmetering.infra.ws.SendNotificationServiceClient;

@Transactional(value = "transactionManager")
@Validated
public class NotificationServiceWs implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceWs.class);

    private final SendNotificationServiceClient sendNotificationServiceClient;

    private final String notificationUrl;

    private final String notificationUsername;

    @Autowired
    private ResponseUrlService responseUrlService;

    public NotificationServiceWs(final SendNotificationServiceClient client, final String notificationUrl,
            final String notificationUsername) {
        this.sendNotificationServiceClient = client;
        this.notificationUrl = notificationUrl;
        this.notificationUsername = notificationUsername;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alliander.osgp.adapter.ws.smartmetering.application.services.
     * INotificationService#sendNotification(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String,
     * com.alliander.osgp.adapter
     * .ws.schema.smartmetering.notification.NotificationType)
     */
    @Override
    public void sendNotification(final String organisationIdentification, final String deviceIdentification,
            final String result, final String correlationUid, final String message,
            final NotificationType notificationType) {

        LOGGER.info("sendNotification called with organisation: {}, correlationUid: {}, type: {}",
                organisationIdentification, correlationUid, notificationType);

        final Notification notification = new Notification();
        // message is null, unless an error occurred
        notification.setMessage(message);
        notification.setResult(result);
        notification.setDeviceIdentification(deviceIdentification);
        notification.setCorrelationUid(correlationUid);
        notification.setNotificationType(notificationType);

        final String url = this.notificationUrl(correlationUid);
        try {
            this.sendNotificationServiceClient.sendNotification(organisationIdentification, notification, url,
                    this.notificationUsername);
        } catch (final WebServiceSecurityException | WebServiceTransportException ex) {
            LOGGER.error("Error while sending Soap message, url:{}, organization:{}, msg:{} ", url,
                    organisationIdentification, ex.getMessage());
        }
    }

    private String notificationUrl(final String correlationUid) {
        final String responseUrl = this.responseUrlService.findResponseUrl(correlationUid);
        if (responseUrl == null) {
            return this.notificationUrl;
        } else {
            this.responseUrlService.deleteResponseUrl(correlationUid);
            return responseUrl;
        }
    }
}

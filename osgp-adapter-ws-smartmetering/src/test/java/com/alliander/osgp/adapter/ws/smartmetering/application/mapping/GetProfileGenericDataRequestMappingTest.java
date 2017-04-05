/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.adapter.ws.smartmetering.application.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.alliander.osgp.adapter.ws.schema.smartmetering.bundle.GetProfileGenericDataRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.common.CaptureObjectDefinitions;
import com.alliander.osgp.adapter.ws.schema.smartmetering.common.ObisCodeValues;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ProfileGenericDataRequestData;

public class GetProfileGenericDataRequestMappingTest {

    private final MonitoringMapper mapper = new MonitoringMapper();

    private static final DateTime BEGIN_DATE = new DateTime(2017, 1, 1, 0, 0, 0, DateTimeZone.UTC);
    private static final DateTime END_DATE = new DateTime(2017, 2, 1, 0, 0, 0, DateTimeZone.UTC);

    @Test
    public void shouldConvertGetProfileGenericDataRequest() {
        final GetProfileGenericDataRequest source = this.makeRequest();
        final ProfileGenericDataRequestData result = this.mapper.map(source, ProfileGenericDataRequestData.class);
        assertNotNull("mapping ProfileGenericDataRequest should not return null", result);
        assertEquals(source.getObisCode().getA(), result.getObisCode().getA());
        assertEquals(source.getObisCode().getB(), result.getObisCode().getB());
        assertEquals(source.getObisCode().getC(), result.getObisCode().getC());
        assertEquals(source.getObisCode().getD(), result.getObisCode().getD());
        assertEquals(source.getObisCode().getE(), result.getObisCode().getE());
        assertEquals(source.getObisCode().getF(), result.getObisCode().getF());
        assertEquals(source.getBeginDate(), this.toGregorianCalendar(new DateTime(result.getBeginDate())));
        assertEquals(source.getEndDate(), this.toGregorianCalendar(new DateTime(result.getEndDate())));
    }

    private GetProfileGenericDataRequest makeRequest() {
        final GetProfileGenericDataRequest result = new GetProfileGenericDataRequest();
        result.setObisCode(this.makeObisCodeValues());
        result.setBeginDate(this.toGregorianCalendar(BEGIN_DATE));
        result.setEndDate(this.toGregorianCalendar(END_DATE));
        result.setSelectedValues(new CaptureObjectDefinitions());
        return result;
    }

    private ObisCodeValues makeObisCodeValues() {
        final ObisCodeValues result = new ObisCodeValues();
        result.setA((short) 1);
        result.setB((short) 2);
        result.setC((short) 3);
        result.setD((short) 4);
        result.setE((short) 5);
        result.setF((short) 6);
        return result;
    }

    private XMLGregorianCalendar toGregorianCalendar(final DateTime dateTime) {
        final GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(dateTime.toDate());
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (final DatatypeConfigurationException e) {
            throw new RuntimeException("error creating XMLGregorianCalendar");
        }
    }
}

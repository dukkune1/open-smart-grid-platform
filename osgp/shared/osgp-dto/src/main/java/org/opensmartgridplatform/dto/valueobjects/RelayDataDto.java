/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects;

import java.io.Serializable;

public class RelayDataDto implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -6823423138978797700L;

    private int index;
    private int totalLightingMinutes;

    public RelayDataDto(final int index, final int totalLightingMinutes) {
        this.index = index;
        this.totalLightingMinutes = totalLightingMinutes;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public int getTotalLightingMinutes() {
        return this.totalLightingMinutes;
    }

    public void setTotalLightingMinutes(final int totalLightingMinutes) {
        this.totalLightingMinutes = totalLightingMinutes;
    }
}

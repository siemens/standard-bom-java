/*
 * Copyright (c) Siemens AG 2019-2023 ALL RIGHTS RESERVED
 */
package com.siemens.sbom.standardbom.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * A test object with different dates.
 */
class ValidTimestamps
{
    @JsonProperty
    Date date1;

    @JsonProperty
    Date date2;

    @JsonProperty
    Date date3;

    @JsonProperty
    Date date4;

    @JsonProperty
    Date date5;

    @JsonProperty
    Date date6;

    @JsonProperty
    Date date7;

    @JsonProperty
    Date date8;
}

package com.concordium.sdk.serializing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class CborMapper {
    public static ObjectMapper INSTANCE = new CBORMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // Needed to deserialize Optional Fields
            .registerModule(new Jdk8Module());
}

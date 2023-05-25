package com.concordium.sdk.serializing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonMapper {
    public final static ObjectMapper INSTANCE =
            new ObjectMapper()
                    // TODO commment this back in after testing
                    //.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .registerModule(new JavaTimeModule())
                    // Needed to deserialize Optional Fields
                    .registerModule(new Jdk8Module());
}

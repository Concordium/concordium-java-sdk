package com.concordium.sdk.serializing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.val;

public final class JsonMapper {

    public final static ObjectMapper MAPPER = new ObjectMapper();

}

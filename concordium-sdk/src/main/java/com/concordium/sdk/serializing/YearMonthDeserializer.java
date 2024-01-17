package com.concordium.sdk.serializing;

import java.io.IOException;
import java.time.YearMonth;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class YearMonthDeserializer extends JsonDeserializer<YearMonth> {

    @Override
    public YearMonth deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String yearMonth = p.readValueAs(String.class);
        String year = yearMonth.subSequence(0, 4).toString();
        String month = yearMonth.substring(4, 6).toString();
        return YearMonth.parse(year + "-" + month);
    }
}

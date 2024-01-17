package com.concordium.sdk.serializing;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class YearMonthSerializer extends StdSerializer<YearMonth> {
    
    private static final String FORMAT = "yyyyMM";

    public YearMonthSerializer() {
        this(null);
    }

    public YearMonthSerializer(Class<YearMonth> t) {
        super(t);
    }

    @Override
    public void serialize(
            YearMonth yearMonth, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString(yearMonth.format(DateTimeFormatter.ofPattern(FORMAT)));
    }
}

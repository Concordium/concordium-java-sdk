package com.concordium.sdk.transactions.smartcontracts.parameters;

import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

/**
 * Wrapper for passing lists as smart contract parameters.<p>
 * Classes extending {@link ListParam} are serializable as smart contract parameters.
 * Contents of the list must be serializable according to the provided {@link Schema}.
 */
@Getter
@JsonSerialize(using = ListParam.ListParamSerializer.class)
public abstract class ListParam extends SchemaParameter {

    /**
     * The actual parameter to be serialized. Extending classes should specify the contents of the list.
     */
    private final List<?> list;

    public ListParam(Schema schema, InitName initName, List<?> list) {
        super(schema, initName);
        this.list = list;
    }

    public ListParam(Schema schema, ReceiveName receiveName, List<?> list) {
        super(schema, receiveName);
        this.list = list;
    }

    public static class ListParamSerializer extends JsonSerializer<ListParam> {
        @Override
        public void serialize(ListParam listParam, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeRaw(JsonMapper.INSTANCE.writeValueAsString(listParam.getList()));
        }
    }
}

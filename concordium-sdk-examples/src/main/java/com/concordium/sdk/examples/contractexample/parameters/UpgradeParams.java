package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

/**
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L150">UpgradeParams</a>' used in the cis2-wCCD contract
 */
@Getter
public class UpgradeParams extends SchemaParameter {

    private final String module;

    @JsonSerialize(using = MigrateSerializer.class)
    private final SchemaParameter migrate; // Hex encode bytes , ReceiveName fås fra schemaparam, optional schema parameter

    public UpgradeParams(Schema schema, ReceiveName receiveName, String module, SchemaParameter migrate) {
        super(schema, receiveName);
        this.module = module;
        migrate.initialize(true);
        this.migrate = migrate;
    }

    public static class MigrateSerializer extends JsonSerializer<SchemaParameter> {
        @Override
        public void serialize(SchemaParameter migrate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("Some");

            jsonGenerator.writeStartArray();

            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(migrate.getReceiveName().getMethod());
            jsonGenerator.writeString(Hex.encodeHexString(migrate.toBytes()));
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndArray();
            jsonGenerator.writeEndObject();

        }
    }
}

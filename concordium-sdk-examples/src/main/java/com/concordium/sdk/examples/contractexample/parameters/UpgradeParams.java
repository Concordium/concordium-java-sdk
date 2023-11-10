package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.responses.modulelist.ModuleRef;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.util.Optional;

/**
 * The parameter type for the contract function `upgrade`.
 * Takes the new module and optionally an entrypoint to call in the new module after triggering the upgrade.
 * The upgrade is reverted if the entrypoint fails.
 * This is useful for doing migration in the same transaction triggering the upgrade.
 * Represents the parameter '<a href="https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/examples/cis2-wccd/src/lib.rs#L150">UpgradeParams</a>' used in the cis2-wCCD contract
 */
@Getter
@ToString
public class UpgradeParams extends SchemaParameter {

    /**
     * The new module reference.
     */
    private final ModuleRef module;
    /**
     * Optional entrypoint to call in the new module after upgrade.
     */
    @JsonSerialize(using = MigrateSerializer.class)
    private final Optional<SchemaParameter> migrate;

    public UpgradeParams(Schema schema, ReceiveName receiveName, ModuleRef module, SchemaParameter migrate) {
        super(schema, receiveName);
        this.module = module;
        if (migrate != null) {
            migrate.initialize(true);
            this.migrate = Optional.of(migrate);
        }
        else {
            this.migrate = Optional.empty();
        }
    }

    public static class MigrateSerializer extends JsonSerializer<Optional<SchemaParameter>> {
        @Override
        public void serialize(Optional<SchemaParameter> migrate, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeStartObject();
            if (migrate.isPresent()) {
                SchemaParameter param = migrate.get();
                jsonGenerator.writeFieldName("Some");

                jsonGenerator.writeStartArray();

                jsonGenerator.writeStartArray();
                jsonGenerator.writeString(param.getReceiveName().getMethod());
                jsonGenerator.writeString(Hex.encodeHexString(param.toBytes()));
                jsonGenerator.writeEndArray();

                jsonGenerator.writeEndArray();
            } else {
                jsonGenerator.writeFieldName("None");
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }
            jsonGenerator.writeEndObject();
        }
    }
}

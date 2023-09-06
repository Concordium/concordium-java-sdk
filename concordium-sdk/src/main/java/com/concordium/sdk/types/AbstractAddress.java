package com.concordium.sdk.types;

import com.concordium.grpc.v2.Address;
import com.concordium.sdk.transactions.AccountType;
import com.concordium.sdk.transactions.SchemaParameter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.Map;

/**
 * An abstract Address.
 * Implementations are either Account - or Contract addresses.
 * <p>
 * If used as parameter for a smart contract, the field containing the Abstract Address must be annotated: <p>
 * '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)' <p>
 * to ensure proper serialization.
 */
@EqualsAndHashCode
public abstract class AbstractAddress {

    @Getter
    private final AccountType type;

    AbstractAddress(AccountType type) {
        this.type = type;
    }

    //not pretty - find a better way of handling this.
    public static AbstractAddress parseAccount(Map<String, Object> o) {
        try {
            if (isContractAddress(o)) {
                val contract = (Map<String, Integer>) o.get("address");
                return new ContractAddress(
                        contract.get("subindex"),
                        contract.get("index"));
            } else {
                return AccountAddress.from(((String) o.get("address")));
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isContractAddress(Map<String, Object> o) {
        return AccountType.from((String) o.get("type")) == AccountType.ADDRESS_CONTRACT;
    }

    public static AbstractAddress from(Address instigator) {
        switch (instigator.getTypeCase()) {
            case ACCOUNT:
                return AccountAddress.from(instigator.getAccount());
            case CONTRACT:
                return ContractAddress.from(instigator.getContract());
            case TYPE_NOT_SET:
                throw new IllegalArgumentException("Account type not set.");
        }
        throw new IllegalArgumentException("Unrecognized account type.");
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

    /**
     * Implements custom serialization for {@link AbstractAddress}. Should be used when creating {@link SchemaParameter} containing {@link AbstractAddress}. <p>
     * To use, annotate field containing {@link AbstractAddress} with <p> '@JsonSerialize(using = AbstractAddress.AbstractAddressJsonSerializer.class)'
     */
    public static class AbstractAddressJsonSerializer extends JsonSerializer<AbstractAddress> {
        @Override
        public void serialize(AbstractAddress address, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            switch (address.getType()){
                case ADDRESS_ACCOUNT:
                    AccountAddress accountAddress = (AccountAddress) address;

                    jsonGenerator.writeStartObject();

                    jsonGenerator.writeFieldName("Account");

                    jsonGenerator.writeStartArray();
                    jsonGenerator.writeString(accountAddress.encoded());
                    jsonGenerator.writeEndArray();

                    jsonGenerator.writeEndObject();
                    break;

                case ADDRESS_CONTRACT:
                    ContractAddress contractAddress = (ContractAddress) address;

                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeFieldName("Contract");
                    jsonGenerator.writeStartArray();
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeNumberField("index", contractAddress.getIndex());
                    jsonGenerator.writeNumberField("subindex", contractAddress.getSubIndex());
                    jsonGenerator.writeEndObject();
                    jsonGenerator.writeEndArray();
                    jsonGenerator.writeEndObject();
                    break;
            }
        }
    }

}

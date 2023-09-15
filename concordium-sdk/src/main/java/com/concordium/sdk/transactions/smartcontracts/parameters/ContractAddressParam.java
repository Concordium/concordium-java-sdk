package com.concordium.sdk.transactions.smartcontracts.parameters;

import com.concordium.sdk.transactions.InitName;
import com.concordium.sdk.transactions.ReceiveName;
import com.concordium.sdk.transactions.smartcontracts.Schema;
import com.concordium.sdk.transactions.smartcontracts.SchemaParameter;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

/**
 * Wrapper class allowing {@link ContractAddress} to be passed directly as a smart contract parameter.
 */
@Getter
@JsonSerialize(using = ContractAddressParam.ContractAddressParamSerializer.class)
public class ContractAddressParam extends SchemaParameter {

    private final ContractAddress contractAddress;

    public ContractAddressParam(Schema schema, InitName initName, ContractAddress contractAddress) {
        super(schema, initName);
        this.contractAddress = contractAddress;
    }

    public ContractAddressParam(Schema schema, ReceiveName receiveName, ContractAddress contractAddress) {
        super(schema, receiveName);
        this.contractAddress = contractAddress;
    }

    public static class ContractAddressParamSerializer extends JsonSerializer<ContractAddressParam> {
        @Override
        public void serialize(ContractAddressParam contractAddressParam, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
            jsonGenerator.writeObject(contractAddressParam.getContractAddress());
        }
    }
}

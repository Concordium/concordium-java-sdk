package com.concordium.sdk.examples.contractexample.parameters;

import com.concordium.sdk.transactions.AccountType;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;

/**
 * The receiving address for a {@link Cis2Transfer}, similar to the {@link AbstractAddress} type, but contains extra information when the receiver address is a {@link ContractAddress}.
 * Represents a '<a href = "https://github.com/Concordium/concordium-rust-smart-contracts/blob/main/concordium-cis2/src/lib.rs#L1017">Receiver</a>' used in {@link Cis2Transfer}.
 */
@JsonSerialize(using = Receiver.ReceiverSerializer.class)
@Getter
public class Receiver {

    /**
     * The type of the account.
     */
    private final AccountType type;
    /**
     * The receiving address. Populated if and only if type is {@link AccountType#ADDRESS_ACCOUNT}.
     */
    private AccountAddress accountAddress;
    /**
     * The receiving address. Populated if and only if type is {@link AccountType#ADDRESS_CONTRACT}.
     */
    private ContractAddress contractAddress;
    /**
     * The function to call on the receiving contract. Populated if and only if type is {@link AccountType#ADDRESS_CONTRACT}.
     */
    private String ownedEntrypointName;

    public Receiver(AccountAddress accountAddress) {
        this.type = AccountType.ADDRESS_ACCOUNT;
        this.accountAddress = accountAddress;
    }

    public Receiver(ContractAddress contractAddress, String ownedEntrypointName) {
        this.type = AccountType.ADDRESS_CONTRACT;
        this.contractAddress = contractAddress;
        this.ownedEntrypointName = ownedEntrypointName;
    }

    public static class ReceiverSerializer extends JsonSerializer<Receiver> {
        @Override
        public void serialize(Receiver receiver, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            switch (receiver.getType()) {
                case ADDRESS_ACCOUNT:
                    jsonGenerator.writeStartObject();

                    jsonGenerator.writeFieldName("Account");

                    jsonGenerator.writeStartArray();

                    jsonGenerator.writeObject(receiver.getAccountAddress());
                    jsonGenerator.writeEndArray();

                    jsonGenerator.writeEndObject();
                    break;
                case ADDRESS_CONTRACT:
                    jsonGenerator.writeStartObject();

                    jsonGenerator.writeFieldName("Contract");

                    jsonGenerator.writeStartArray();

                    jsonGenerator.writeObject(receiver.getContractAddress());
                    jsonGenerator.writeString(receiver.getOwnedEntrypointName());

                    jsonGenerator.writeEndArray();

                    jsonGenerator.writeEndObject();

                    break;
            }
        }
    }
}

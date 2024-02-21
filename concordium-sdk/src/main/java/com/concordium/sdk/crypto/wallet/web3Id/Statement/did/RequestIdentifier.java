package com.concordium.sdk.crypto.wallet.web3Id.Statement.did;

import java.io.IOException;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.StatementType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonDeserialize(using = RequestIdentifierDeserializer.class)
public abstract class RequestIdentifier {
    Network network;

    public abstract StatementType getType();
}

class RequestIdentifierDeserializer extends JsonDeserializer<RequestIdentifier> {
    @Override
    public RequestIdentifier deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        System.out.println("Test");
        JsonNode reference = p.getCodec().readTree(p);
        if (!reference.isTextual()) {
            throw new JsonParseException(p, "RequestIdentifier must be deserialize from a String");
        }
        String did = reference.asText();

        AccountRequestIdentifier accountDID = AccountRequestIdentifier.tryFromString(did);
        if (accountDID != null) {
            return accountDID;
        }
        Web3IssuerRequestIdentifier web3DID = Web3IssuerRequestIdentifier.tryFromString(did);
        if (web3DID != null) {
            return web3DID;
        }
        throw new JsonParseException(p, "Given string is not a valid request statement did");
    }
}

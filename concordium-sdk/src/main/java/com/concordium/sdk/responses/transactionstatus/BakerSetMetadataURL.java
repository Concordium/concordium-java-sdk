package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BakerSetMetadataURL extends AbstractBakerResult {
    private final String metadataUrl;

    @JsonCreator
    BakerSetMetadataURL(@JsonProperty("bakerId") long bakerId,
                        @JsonProperty("account") AccountAddress bakerAccount,
                        @JsonProperty("metadataURL") String metadataUrl){
        super(bakerId, bakerAccount);
        this.metadataUrl = metadataUrl;
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_METADATA_URL;
    }
}

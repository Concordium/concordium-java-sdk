package com.concordium.sdk.responses.transactionstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BakerSetMetadataURL extends AbstractBakerResult {
    private final String metadataUrl;

    @JsonCreator
    BakerSetMetadataURL(@JsonProperty("bakerId") String bakerId,
                        @JsonProperty("account") String bakerAccount,
                        @JsonProperty("metadataURL") String metadataUrl){
        super(bakerId, bakerAccount);
        this.metadataUrl = metadataUrl;
    }
}

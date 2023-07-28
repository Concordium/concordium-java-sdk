package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BakerSetMetadataURL extends AbstractBakerResult {
    private final String metadataUrl;

    @JsonCreator
    BakerSetMetadataURL(@JsonProperty("bakerId") AccountIndex bakerId,
                        @JsonProperty("account") AccountAddress bakerAccount,
                        @JsonProperty("metadataURL") String metadataUrl) {
        super(bakerId, bakerAccount);
        this.metadataUrl = metadataUrl;
    }

    public static BakerSetMetadataURL from(BakerEvent.BakerSetMetadataUrl bakerSetMetadataUrl, AccountAddress sender) {
        return BakerSetMetadataURL
                .builder()
                .metadataUrl(bakerSetMetadataUrl.getUrl())
                .bakerId(BakerId.from(bakerSetMetadataUrl.getBakerId()))
                .account(sender)
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_METADATA_URL;
    }
}

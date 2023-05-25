package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
public class BakerSetMetadataURL extends AbstractBakerResult {
    private final String metadataUrl;

    @JsonCreator
    BakerSetMetadataURL(@JsonProperty("bakerId") AccountIndex bakerId,
                        @JsonProperty("account") AccountAddress bakerAccount,
                        @JsonProperty("metadataURL") String metadataUrl){
        super(bakerId, bakerAccount);
        this.metadataUrl = metadataUrl;
    }

    /**
     * Parses {@link BakerEvent.BakerSetMetadataUrl} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetMetadataURL}.
     * @param bakerSetMetadataUrl  {@link BakerEvent.BakerSetMetadataUrl} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetMetadataURL}.
     */
    public static BakerSetMetadataURL parse(BakerEvent.BakerSetMetadataUrl bakerSetMetadataUrl, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetMetadataURL.builder()
                .metadataUrl(bakerSetMetadataUrl.getUrl())
                .bakerId(AccountIndex.from(bakerSetMetadataUrl.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_METADATA_URL;
    }
}

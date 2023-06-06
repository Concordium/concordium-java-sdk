package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEvent;
import com.concordium.sdk.responses.transactionevent.accounttransactionresults.BakerEventType;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Updated metadata URL for a baker pool.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
public class BakerSetMetadataURL extends AbstractBakerResult implements BakerEvent {

    /**
     * The URL.
     */
    private final String metadataUrl;

    @Builder
    @JsonCreator
    BakerSetMetadataURL(@JsonProperty("bakerId") AccountIndex bakerId,
                        @JsonProperty("account") AccountAddress bakerAccount,
                        @JsonProperty("metadataURL") String metadataUrl){
        super(bakerId, bakerAccount);
        this.metadataUrl = metadataUrl;
    }

    /**
     * Parses {@link com.concordium.grpc.v2.BakerEvent.BakerSetMetadataUrl} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetMetadataURL}.
     * @param bakerSetMetadataUrl  {@link com.concordium.grpc.v2.BakerEvent.BakerSetMetadataUrl} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetMetadataURL}.
     */
    public static BakerSetMetadataURL parse(com.concordium.grpc.v2.BakerEvent.BakerSetMetadataUrl bakerSetMetadataUrl, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetMetadataURL.builder()
                .metadataUrl(bakerSetMetadataUrl.getUrl())
                .bakerId(AccountIndex.from(bakerSetMetadataUrl.getBakerId().getValue()))
                .bakerAccount(AccountAddress.parse(sender))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_METADATA_URL;
    }

    @Override
    public BakerEventType getBakerEventType() {
        return BakerEventType.BAKER_SET_METADATA_URL;
    }
}

package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * The Baker updated its {@link OpenStatus}
 */
@ToString
@Getter
@SuperBuilder
public class BakerSetOpenStatus extends AbstractBakerResult {

    /**
     * The updated {@link OpenStatus} for the baker.
     */
    private final OpenStatus openStatus;

    @JsonCreator
    BakerSetOpenStatus(@JsonProperty("bakerId") AccountIndex bakerId,
                       @JsonProperty("account") AccountAddress bakerAccount,
                       @JsonProperty("openStatus") OpenStatus openStatus) {
        super(bakerId, bakerAccount);
        this.openStatus = openStatus;
    }

    /**
     * Parses {@link BakerEvent.BakerSetOpenStatus} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetOpenStatus}.
     * @param bakerSetOpenStatus {@link BakerEvent.BakerSetOpenStatus} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetOpenStatus}.
     */
    public static BakerSetOpenStatus parse(BakerEvent.BakerSetOpenStatus bakerSetOpenStatus, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetOpenStatus.builder()
                .bakerId(AccountIndex.from(bakerSetOpenStatus.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .openStatus(OpenStatus.parse(bakerSetOpenStatus.getOpenStatus()))
                .build();

    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_OPEN_STATUS;
    }
}

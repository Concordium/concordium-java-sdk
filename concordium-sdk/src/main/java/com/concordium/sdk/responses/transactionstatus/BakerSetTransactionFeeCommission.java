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
 * Updated transaction fee commission for a baker pool.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BakerSetTransactionFeeCommission extends AbstractBakerResult implements BakerEvent {

    /**
     * The transaction fee commission
     */
    private final PartsPerHundredThousand feeCommission;

    @Builder
    @JsonCreator
    BakerSetTransactionFeeCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                     @JsonProperty("account") AccountAddress bakerAccount,
                                     @JsonProperty("transactionFeeCommission") PartsPerHundredThousand feeCommission) {
        super(bakerId, bakerAccount);
        this.feeCommission = feeCommission;

    }

    /**
     * Parses {@link com.concordium.grpc.v2.BakerEvent.BakerSetTransactionFeeCommission} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetTransactionFeeCommission}.
     * @param bakerSetTransactionFeeCommission {@link com.concordium.grpc.v2.BakerEvent.BakerSetTransactionFeeCommission} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetTransactionFeeCommission}
     */
    public static BakerSetTransactionFeeCommission parse(com.concordium.grpc.v2.BakerEvent.BakerSetTransactionFeeCommission bakerSetTransactionFeeCommission, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetTransactionFeeCommission.builder()
                .bakerId(AccountIndex.from(bakerSetTransactionFeeCommission.getBakerId().getValue()))
                .bakerAccount(AccountAddress.parse(sender))
                .feeCommission(PartsPerHundredThousand.parse(bakerSetTransactionFeeCommission.getTransactionFeeCommission()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_TRANSACTION_FEE_COMMISSION;
    }

    @Override
    public BakerEventType getBakerEventType() {
        return BakerEventType.BAKER_SET_TRANSACTION_FEE_COMMISSION;
    }
}

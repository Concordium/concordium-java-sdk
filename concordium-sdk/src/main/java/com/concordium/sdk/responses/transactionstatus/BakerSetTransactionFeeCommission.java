package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerEvent;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Updated transaction fee commission for a baker pool.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BakerSetTransactionFeeCommission extends AbstractBakerResult {

    /**
     * The transaction fee commission
     */
    private final PartsPerHundredThousand feeCommission;

    @JsonCreator
    BakerSetTransactionFeeCommission(@JsonProperty("bakerId") AccountIndex bakerId,
                                     @JsonProperty("account") AccountAddress bakerAccount,
                                     @JsonProperty("transactionFeeCommission") PartsPerHundredThousand feeCommission) {
        super(bakerId, bakerAccount);
        this.feeCommission = feeCommission;

    }

    /**
     * Parses {@link BakerEvent.BakerSetTransactionFeeCommission} and {@link com.concordium.grpc.v2.AccountAddress} to {@link BakerSetTransactionFeeCommission}.
     * @param bakerSetTransactionFeeCommission {@link BakerEvent.BakerSetTransactionFeeCommission} returned by the GRPC V2 API.
     * @param sender {@link com.concordium.grpc.v2.AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerSetTransactionFeeCommission}
     */
    public static BakerSetTransactionFeeCommission parse(BakerEvent.BakerSetTransactionFeeCommission bakerSetTransactionFeeCommission, com.concordium.grpc.v2.AccountAddress sender) {
        return BakerSetTransactionFeeCommission.builder()
                .bakerId(AccountIndex.from(bakerSetTransactionFeeCommission.getBakerId().getValue()))
                .account(AccountAddress.parse(sender))
                .feeCommission(PartsPerHundredThousand.parse(bakerSetTransactionFeeCommission.getTransactionFeeCommission()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_SET_TRANSACTION_FEE_COMMISSION;
    }
}

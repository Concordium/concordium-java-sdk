package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import com.concordium.sdk.responses.transactionstatus.EncryptedAmountsRemovedResult;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * An account transferred part of its encrypted balance to its public balance.
 * This is the result of a successful TransferToPublic transaction.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class TransferredToPublicResult implements AccountTransactionResult {

    /**
     * Details of the removed encrypted amount.
     */
    private EncryptedAmountsRemovedResult removed;

    /**
     * The amount transferred from encrypted to public balance.
     */
    private CCDAmount amount;

    /**
     * Parses {@link AccountTransactionEffects.TransferredToPublic} to {@link TransferredToPublicResult}.
     * @param transferredToPublic {@link AccountTransactionEffects.TransferredToPublic} returned by the GRPC V2 API.
     * @return parsed {@link TransferredToPublicResult}.
     */
    public static TransferredToPublicResult parse(AccountTransactionEffects.TransferredToPublic transferredToPublic) {
        return TransferredToPublicResult.builder()
                .removed(EncryptedAmountsRemovedResult.parse(transferredToPublic.getRemoved()))
                .amount(CCDAmount.fromMicro(transferredToPublic.getAmount().getValue()))
                .build();

    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.TRANSFER_TO_PUBLIC;
    }
}

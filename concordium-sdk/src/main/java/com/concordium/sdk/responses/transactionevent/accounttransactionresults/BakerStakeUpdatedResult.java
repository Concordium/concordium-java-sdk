package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountTransactionEffects;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

@Builder
@ToString
@EqualsAndHashCode
public class BakerStakeUpdatedResult implements AccountTransactionResult {

    /**
     * If the stake was updated (that is, it changed and did not stay the same) then this is present.
     * Otherwise, it is not present.
     */
    private BakerStakeUpdatedData update;

    /**
     * Returns {@link Optional} of {@link BakerStakeUpdatedData} if present.
     * Otherwise, returns Empty {@link Optional}
     * @return {@link Optional} of {@link BakerStakeUpdatedData} if present. Otherwise Empty {@link Optional}
     */
    public Optional<BakerStakeUpdatedData> getUpdate() {
        return Optional.ofNullable(update);
    }

    /**
     * Parses {@link AccountTransactionEffects.BakerStakeUpdated} mto {@link BakerStakeUpdatedResult}.
     * @param bakerStakeUpdated {@link AccountTransactionEffects.BakerStakeUpdated} returned by the GRPC V2 API.
     * @return parsed {@link BakerStakeUpdatedResult}.
     */
    public static BakerStakeUpdatedResult parse(AccountTransactionEffects.BakerStakeUpdated bakerStakeUpdated) {
        if (bakerStakeUpdated.hasUpdate()) {
            return BakerStakeUpdatedResult.builder()
                    .update(BakerStakeUpdatedData.parse(bakerStakeUpdated.getUpdate()))
                    .build();
        }
        return BakerStakeUpdatedResult.builder().build();
    }
    @Override
    public TransactionType getResultType() {
        return TransactionType.UPDATE_BAKER_STAKE;
    }
}

package com.concordium.sdk.responses.transactionevent.accounttransactionresults;

import com.concordium.grpc.v2.AccountAddress;
import com.concordium.grpc.v2.AccountTransactionEffects;
import com.google.common.collect.ImmutableList;
import lombok.*;

import java.util.List;

/**
 * A baker was configured.
 * The details of what happened are stored in the list of BakerEvents
 */
@Builder
@EqualsAndHashCode
@Getter
@ToString
public class BakerConfiguredResult implements AccountTransactionResult {

    private List<BakerEvent> events;

    /**
     * Parses {@link AccountTransactionEffects.BakerConfigured} and {@link AccountAddress} to {@link BakerConfiguredResult}.
     * @param bakerConfigured {@link AccountTransactionEffects.BakerConfigured} returned by the GRPC V2 API.
     * @param sender {@link AccountAddress} returned by the GRPC V2 API.
     * @return parsed {@link BakerConfiguredResult}.
     */
    public static BakerConfiguredResult parse(AccountTransactionEffects.BakerConfigured bakerConfigured, AccountAddress sender) {
        val events = new ImmutableList.Builder<BakerEvent>();
        val bakerEvents = bakerConfigured.getEventsList();
        bakerEvents.forEach(bakerEvent -> events.add(BakerEvent.parse(bakerEvent, sender)));

        return BakerConfiguredResult.builder()
                .events(events.build())
                .build();
    }

    @Override
    public TransactionType getResultType() {
        return TransactionType.CONFIGURE_BAKER;
    }
}

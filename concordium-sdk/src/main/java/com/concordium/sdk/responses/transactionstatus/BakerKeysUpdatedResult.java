package com.concordium.sdk.responses.transactionstatus;

import com.concordium.grpc.v2.BakerKeysEvent;
import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.responses.BakerId;
import com.concordium.sdk.types.AccountAddress;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public final class BakerKeysUpdatedResult extends AbstractBakerChangeResult {
    public static BakerKeysUpdatedResult from(BakerKeysEvent bakerKeysUpdated, AccountAddress sender) {
        return BakerKeysUpdatedResult
                .builder()
                .bakerId(BakerId.from(bakerKeysUpdated.getBakerId()))
                .account(sender)
                .electionKey(bakerKeysUpdated.getElectionKey().toByteArray())
                .aggregationKey(bakerKeysUpdated.getAggregationKey().toByteArray())
                .signKey(ED25519PublicKey.from(bakerKeysUpdated.getSignKey()))
                .build();
    }

    @Override
    public TransactionResultEventType getType() {
        return TransactionResultEventType.BAKER_KEYS_UPDATED;
    }
}

package com.concordium.sdk.responses;

import com.concordium.sdk.responses.accountinfo.PendingChange;
import com.concordium.sdk.transactions.AccountAddress;
import com.concordium.sdk.transactions.CCDAmount;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class DelegatorInfo {
    private final AccountAddress account;
    private final CCDAmount stake;
    private final Optional<PendingChange> pendingChange;
}

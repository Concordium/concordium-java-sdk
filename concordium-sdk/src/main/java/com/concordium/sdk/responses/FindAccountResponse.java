package com.concordium.sdk.responses;

import com.concordium.sdk.requests.Range;
import com.concordium.sdk.responses.accountinfo.AccountInfo;
import com.concordium.sdk.transactions.Hash;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.UInt64;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Response type for {@link com.concordium.sdk.ClientV2#findAccountCreation(Range, AccountAddress)}.
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class FindAccountResponse {
    private final UInt64 absoluteBlockHeight;
    private final Hash blockHash;
    private final AccountInfo accountInfo;
}

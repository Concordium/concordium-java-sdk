package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.transactions.AccountAddress;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBakerKeysJniInput {
    private final AccountAddress sender;
    private final BakerKeysJniOutput keys;
}

package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.transactions.AccountAddress;
import lombok.Builder;
import lombok.Data;

/**
 * Configure the account as a baker. Only valid for protocol version 4 and up.
 */
@Data
@Builder
public class ConfigureBakerKeysJniInput {
    private final AccountAddress sender;
    private final BakerKeysJniOutput keys;
}

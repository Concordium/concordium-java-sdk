package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.types.AccountAddress;
import lombok.Builder;
import lombok.Data;

/**
 * Configure the account as a baker. Only valid for protocol version 4 and up.
 */
@Data
@Builder
public class ConfigureBakerKeysJniInput {
    /**
     * The address of the account that will be configured as a baker
     */
    private final AccountAddress sender;
    /**
     * The baker keys that will be configured for the account
     */
    private final BakerKeys keys;
}

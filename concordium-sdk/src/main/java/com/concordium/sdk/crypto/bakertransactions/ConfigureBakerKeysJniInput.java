package com.concordium.sdk.crypto.bakertransactions;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

/**
 * Configure the account as a baker. Only valid for protocol version 4 and up.
 */
@Data
public class ConfigureBakerKeysJniInput {

    /**
     * The address of the account that will be configured as a baker
     */
    private final ConfigureBakerAccountAddress sender;

    /**
     * The baker keys that will be configured for the account
     */
    private final BakerKeysJniOutput keys;

    @Builder
    public ConfigureBakerKeysJniInput(AccountAddress sender, BakerKeysJniOutput keys) {
        this.sender = new ConfigureBakerAccountAddress(sender);
        this.keys = keys;
    }

    /**
     * Document why I am here...
     */
    private static class ConfigureBakerAccountAddress {
        private final AccountAddress address;

        private ConfigureBakerAccountAddress(AccountAddress address) {
            this.address = address;
        }

        @JsonValue
        String getAddress () {
            return this.address.encoded();
        }
    }
}

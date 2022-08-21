package com.concordium.sdk.responses.identityproviders;

import lombok.Data;
import lombok.Getter;

/**
 * Description either of an anonymity revoker or identity provider. Metadata that should be visible on the chain.
 */
@Data
@Getter
public class IpDescription {
    /**
     * Name of Identity Provider.
     */
    private String name;

    /**
     * Url of Identity Provider.
     */
    private String url;

    /**
     * Description of Identity Provider.
     */
    private String description;
}

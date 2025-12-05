package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("RevealAttribute")
public class RevealStatement extends AtomicStatement {
    private final String attributeTag;

    @Override
    @SneakyThrows
    public boolean canBeProvedBy(IdentityObject identityObject) {
        try {
            // Attempt to get the attribute
            this.getAttributeValue(identityObject);
        } catch (MissingAttributeException e) {
            // If the identityObject does not have the relevant attribute, it does not satisfy the statement 
            return false;
        }
        // With a reveal statement, the only requirement is that the identity has the attribute.
        return true;
    }
}

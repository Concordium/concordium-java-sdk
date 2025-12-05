package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("AttributeInSet")
public class MembershipStatement extends AtomicStatement implements SetStatement {
    private final String attributeTag;
    private final List<CredentialAttribute> set;

    @Override
    @SneakyThrows
    public boolean canBeProvedBy(IdentityObject identityObject) {
        try {
            CredentialAttribute value = this.getAttributeValue(identityObject);
            return set.contains(value);
        } catch (MissingAttributeException e) {
            // If the identityObject does not have the relevant attribute, it does not satisfy the statement 
            return false;
        }
    }
}

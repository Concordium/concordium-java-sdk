package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * This special variant of a {@link RevealStatement} is only used in the verifiable presentation input.
 * It is not meant to be requested, as it already contains the revealed value.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("AttributeValue")
public class AttributeValueStatement extends RevealStatement {
    private final String attributeValue;

    public AttributeValueStatement(RevealStatement revealStatement,
                                   IdentityObject identityObject) {
        this(revealStatement, identityObject.getAttributeList().getChosenAttributes());
    }

    @SneakyThrows
    public AttributeValueStatement(RevealStatement revealStatement,
                                   Map<AttributeType, String> attributeValues) {
        super(revealStatement.getAttributeTag());
        AttributeType attributeType = AttributeType.fromJSON(revealStatement.getAttributeTag());
        if (!attributeValues.containsKey(attributeType)) {
            throw new MissingAttributeException(attributeType);
        }
        this.attributeValue = attributeValues.get(attributeType);
    }

    @JsonCreator
    public AttributeValueStatement(String attributeTag, String attributeValue) {
        super(attributeTag);
        this.attributeValue = attributeValue;
    }
}

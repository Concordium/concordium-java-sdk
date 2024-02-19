package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes ({@Type (value = RevealStatement.class), @Type (value = RangeStatement.class), @Type (value = MembershipStatement.class), @Type (value = NonMembershipStatement.class)})
public abstract class AtomicStatement {
   @JsonProperty("attributeTag")
   public abstract String getAttributeTag();

   // TODO: add overload for web3Id credential
   protected CredentialAttribute getAttributeValue(IdentityObject identityObject) throws Exception {
      AttributeType type = AttributeType.fromJSON(this.getAttributeTag());
      String raw = identityObject.getAttributeList().getChosenAttributes().get(type);
      return CredentialAttribute.builder().value(raw).type(CredentialAttribute.CredentialAttributeType.STRING).build();

   }

   // TODO: add overload for web3Id credential
   public abstract boolean canBeProvedBy(IdentityObject identityObject) throws Exception;
}

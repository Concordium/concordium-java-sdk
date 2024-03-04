package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.concordium.sdk.crypto.wallet.identityobject.IdentityObject;
import com.concordium.sdk.crypto.wallet.identityobject.MissingAttributeException;
import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;
import com.concordium.sdk.responses.accountinfo.credential.AttributeType;

@JsonTypeInfo(use = NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes ({@Type (value = RevealStatement.class), @Type (value = RangeStatement.class), @Type (value = MembershipStatement.class), @Type (value = NonMembershipStatement.class)})
public abstract class AtomicStatement {
   @JsonProperty("attributeTag")
   public abstract String getAttributeTag();

   // TODO: add overload for web3Id credential
   protected CredentialAttribute getAttributeValue(IdentityObject identityObject) throws JsonProcessingException, JsonParseException, MissingAttributeException {
      AttributeType type = AttributeType.fromJSON(this.getAttributeTag());
      String raw = identityObject.getChosenAttribute(type);
      return CredentialAttribute.builder().value(raw).type(CredentialAttribute.CredentialAttributeType.STRING).build();
   }

   // TODO: add overload for web3Id credential
   public abstract boolean canBeProvedBy(IdentityObject identityObject) throws Exception;
}

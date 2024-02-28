package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = As.PROPERTY, property = "type", visible = true)
@JsonSubTypes ({@Type (value = RevealStatement.class), @Type (value = RangeStatement.class), @Type (value = MembershipStatement.class), @Type (value = NonMembershipStatement.class)})
public abstract class AtomicStatement {
   @JsonProperty("attributeTag")
   public abstract String getAttributeTag();
}

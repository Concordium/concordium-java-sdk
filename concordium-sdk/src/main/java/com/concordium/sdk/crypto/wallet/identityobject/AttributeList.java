package com.concordium.sdk.crypto.wallet.identityobject;

import java.time.YearMonth;
import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.serializing.YearMonthDeserializer;
import com.concordium.sdk.serializing.YearMonthSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AttributeList {
    
    private Map<AttributeType, String> chosenAttributes;
    
    @JsonDeserialize(using = YearMonthDeserializer.class)
    @JsonSerialize(using = YearMonthSerializer.class)
    YearMonth validTo;

    @JsonDeserialize(using = YearMonthDeserializer.class)
    @JsonSerialize(using = YearMonthSerializer.class)
    YearMonth createdAt;
    int maxAccounts;

}

package com.concordium.sdk.responsetypes.accountinfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EncryptedAmount {
    private List<String> incomingAmounts;
    private String selfAmount;
    private int startIndex;
}

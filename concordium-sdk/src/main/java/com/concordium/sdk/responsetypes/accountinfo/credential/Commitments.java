package com.concordium.sdk.responsetypes.accountinfo.credential;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
class Commitments {
    private String cmmPrf;
    private String cmmCredCounter;
    private List<String> cmmIdCredSecSharingCoeff;
    private Attributes cmmAttributes;
    private String cmmMaxAccounts;
}

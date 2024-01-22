package com.concordium.sdk.crypto.wallet.identityobject;

import java.util.List;

import lombok.Getter;

@Getter
public class ChoiceArData {

    List<Integer> arIdentities;
    int threshold;

}

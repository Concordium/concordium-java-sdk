package com.concordium.sdk.crypto.wallet.web3Id.Statement;

import java.util.List;

import com.concordium.sdk.crypto.wallet.web3Id.CredentialAttribute;

public interface SetStatement {
    public String getAttributeTag();
    public List<CredentialAttribute> getSet();
}

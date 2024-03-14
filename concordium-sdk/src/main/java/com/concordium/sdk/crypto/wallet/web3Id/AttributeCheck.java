package com.concordium.sdk.crypto.wallet.web3Id;


public interface AttributeCheck {
    /**
     * This check should throw an exception if the given attribute value is not valid.
     * @param tag the attribute tag for the statement
     * @param value the attribute value used in the statement
     * @throws Exception to indicate the attribute is not valid.
    */
    public void checkAttribute(String tag, CredentialAttribute value) throws Exception;
}

package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.Map;

import com.concordium.sdk.responses.accountinfo.credential.AttributeType;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@JsonTypeName("account")
public class AccountCommitmentInput extends CommitmentInput {
    private UInt32 issuer;
    private Map<AttributeType,String> values;
    private Map<AttributeType, String> randomness;
}

package com.concordium.sdk.crypto.wallet.web3Id.Statement.did;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.StatementType;
import com.concordium.sdk.transactions.CredentialRegistrationId;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountRequestIdentifier extends RequestIdentifier {
    private final CredentialRegistrationId credId;
    private static final Pattern pattern = Pattern.compile( "^" + getDID("(mainnet|testnet)", "([a-zA-Z0-9]*)") + "$");

    @Builder
    public AccountRequestIdentifier(Network network, CredentialRegistrationId credId) {
        super(network);
        this.credId = credId;
    }

    private static String getDID(String network, String credId) {
        return "did:ccd:" + network + ":cred:" + credId;
    }

    @JsonValue
    @Override
    public String toString() {
        return getDID(network.getValue().toLowerCase(), credId.getEncoded());
    }

    @Nullable
    public static AccountRequestIdentifier tryFromString(String did) {
        Matcher matcher = pattern.matcher(did);
        if (matcher.matches()) {
            Network network = Network.fromLowerCase(matcher.group(1));
            CredentialRegistrationId credId = CredentialRegistrationId.from(matcher.group(2));
            return new AccountRequestIdentifier(network, credId);
        }
        return null;
    }

    @Override
    public StatementType getType() {
        return StatementType.Credential;
    }
}

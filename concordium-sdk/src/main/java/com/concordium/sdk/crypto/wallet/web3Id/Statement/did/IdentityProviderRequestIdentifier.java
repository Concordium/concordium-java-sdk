package com.concordium.sdk.crypto.wallet.web3Id.Statement.did;

import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.StatementType;
import com.concordium.sdk.types.UInt32;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.val;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(callSuper = true)
public class IdentityProviderRequestIdentifier extends RequestIdentifier {
    private final UInt32 ipIdentity;
    private static final Pattern pattern = Pattern.compile( "^" + getDID("(mainnet|testnet)", "(\\d+)") + "$");

    @Builder
    public IdentityProviderRequestIdentifier(Network network, UInt32 ipIdentity) {
        super(network);
        this.ipIdentity = ipIdentity;
    }

    private static String getDID(String network, String ipIdentity) {
        return "did:ccd:" + network + ":idp:" + ipIdentity;
    }

    @JsonValue
    @Override
    public String toString() {
        return getDID(network.getValue().toLowerCase(), ipIdentity.toString());
    }

    @Nullable
    public static IdentityProviderRequestIdentifier tryFromString(String did) {
        Matcher matcher = pattern.matcher(did);
        if (matcher.matches()) {
            Network network = Network.fromLowerCase(matcher.group(1));
            UInt32 ipIdentity = UInt32.from(matcher.group(2));
            return new IdentityProviderRequestIdentifier(network, ipIdentity);
        }
        return null;
    }

    @JsonCreator
    public static IdentityProviderRequestIdentifier fromString(String did) {
        val parsed = tryFromString(did);
        if (parsed == null) {
            throw new IllegalArgumentException("Given string is not a valid identity provider DID");
        }
        return parsed;
    }

    @Override
    public StatementType getType() {
        return StatementType.IdentityProvider;
    }
}

package com.concordium.sdk.crypto.wallet.web3Id.Statement.did;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.commons.codec.binary.Hex;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.StatementType;
import com.concordium.sdk.types.ContractAddress;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Web3IssuerRequestIdentifier extends RequestIdentifier {
    private ContractAddress address;
    private ED25519PublicKey publicKey;
    private static Pattern pattern = Pattern.compile("^" + getDID("(mainnet|testnet)", "([0-9]*)", "([0-9]*)", "([a-zA-Z0-9]*)") + "$", Pattern.CASE_INSENSITIVE);

    @Builder
    public Web3IssuerRequestIdentifier(Network network, ContractAddress address, ED25519PublicKey publicKey) {
        super(network);
        this.address = address;
        this.publicKey = publicKey;
    }

    private static String getDID(String network, String index, String subindex, String publicKey) {
        return "did:ccd:" +
                network +
                ":sci:" +
                index +
                ":" +
                subindex +
                "/credentialEntry/" +
                publicKey;
    }

    @JsonValue
    @Override
    public String toString() {
        return getDID(network.getValue().toLowerCase(), Long.toUnsignedString(address.getIndex()), Long.toUnsignedString(address.getSubIndex()),
                Hex.encodeHexString(publicKey.getRawBytes()));
    }

    @Nullable
    public static Web3IssuerRequestIdentifier tryFromString(String did) {
        Matcher matcher = pattern.matcher(did);
        if (matcher.matches()) {
            Network network = Network.fromLowerCase(matcher.group(1));
            long index = Long.parseUnsignedLong(matcher.group(2));
            long subindex = Long.parseUnsignedLong(matcher.group(3));
            ED25519PublicKey publicKey = ED25519PublicKey.from(matcher.group(4));
            return new Web3IssuerRequestIdentifier(network, ContractAddress.from(index, subindex), publicKey);
        }
        return null;
    }

    @Override
    public StatementType getType() {
        return StatementType.SmartContract;
    }
}

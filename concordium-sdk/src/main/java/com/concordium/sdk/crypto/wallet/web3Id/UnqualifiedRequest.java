package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.concordium.sdk.crypto.wallet.Network;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.QualifiedRequestStatement;
import com.concordium.sdk.crypto.wallet.web3Id.Statement.UnqualifiedRequestStatement;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UnqualifiedRequest {
    private final String challenge;
    private final List<UnqualifiedRequestStatement> credentialStatements;

    public QualifiedRequest qualify(List<String> credIds, Network network) {
        if (credIds.size() != credentialStatements.size()) {
            throw new IllegalArgumentException("List of credIds must be same size as credentialStatements");
        }
        List<QualifiedRequestStatement> qualifiedStatements = IntStream.range(0, credentialStatements.size())
                .mapToObj(index -> credentialStatements.get(index).qualify(credIds.get(index), network))
                .collect(Collectors.toList());
        return QualifiedRequest.builder().challenge(challenge).credentialStatements(qualifiedStatements).build();
    }
}

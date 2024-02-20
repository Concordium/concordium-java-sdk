package com.concordium.sdk.crypto.wallet.web3Id;

import java.util.List;
import java.util.stream.Collectors;
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

    /**
     * Creates a qualified request, that can be used to generate a presentation.
     * @param qualifier function to qualify the individual request statements.
     * @return a QualifiedRequest with the same challenge, and with the statements
     *         transformed using the qualifier.
     */
    public QualifiedRequest qualify(Qualifier qualifier) {
        List<QualifiedRequestStatement> qualifiedStatements = credentialStatements.stream().map(qualifier::qualify)
                .collect(Collectors.toList());
        return QualifiedRequest.builder().challenge(challenge).credentialStatements(qualifiedStatements).build();
    }

    static interface Qualifier {
        QualifiedRequestStatement qualify(UnqualifiedRequestStatement statement);
    }
}

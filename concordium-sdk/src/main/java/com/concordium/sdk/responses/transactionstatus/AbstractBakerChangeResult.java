package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.crypto.ed25519.ED25519PublicKey;
import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.codec.binary.Hex;

/**
 * An event that is emitted when keys of a baker has changed (or the baker has been registered)
 */
@Getter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBakerChangeResult extends AbstractBakerResult {

    /**
     * The public VRF key of the baker.
     */
    private final byte[] electionKey;

    /**
     * The public BLS key of the baker.
     */
    private final byte[] aggregationKey;

    /**
     * The public eddsa key of the baker.
     */
    private final ED25519PublicKey signKey;

    @SneakyThrows
    @JsonCreator
    AbstractBakerChangeResult(@JsonProperty("bakerId") AccountIndex bakerId,
                              @JsonProperty("account") AccountAddress account,
                              @JsonProperty("electionKey") String electionKey,
                              @JsonProperty("aggregationKey") String aggregationKey,
                              @JsonProperty("signKey") String signKey) {
        super(bakerId, account);
        this.electionKey = Hex.decodeHex(electionKey);
        this.aggregationKey = Hex.decodeHex(aggregationKey);
        this.signKey = ED25519PublicKey.from(signKey);
    }
}

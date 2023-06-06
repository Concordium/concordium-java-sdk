package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.codec.binary.Hex;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBakerChangeResult extends AbstractBakerResult {

    /**
     * The new public key for verifying whether the baker won the block lottery.
     */
    private final byte[] electionKey;
    /**
     * The new public key for verifying finalization records.
     */
    private final byte[] aggregationKey;
    /**
     * The new public key for verifying block signatures.
     */
    private final byte[] signKey;

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
        this.signKey = Hex.decodeHex(signKey);
    }

    AbstractBakerChangeResult(AccountIndex bakerId, AccountAddress account, byte[] electionKey, byte[] aggregationKey, byte[] signKey) {
        super(bakerId, account);
        this.electionKey = electionKey;
        this.aggregationKey = aggregationKey;
        this.signKey = signKey;
    }
}

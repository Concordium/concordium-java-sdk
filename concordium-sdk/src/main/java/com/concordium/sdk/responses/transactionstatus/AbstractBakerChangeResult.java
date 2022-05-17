package com.concordium.sdk.responses.transactionstatus;

import com.concordium.sdk.responses.AccountIndex;
import com.concordium.sdk.transactions.AccountAddress;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.codec.binary.Hex;

@Getter
@ToString
public abstract class AbstractBakerChangeResult extends AbstractBakerResult {
    private final byte[] electionKey;
    private final byte[] aggregationKey;
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
}

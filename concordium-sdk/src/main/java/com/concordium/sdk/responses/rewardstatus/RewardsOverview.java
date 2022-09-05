package com.concordium.sdk.responses.rewardstatus;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import lombok.val;

import java.util.Date;
import java.util.Optional;

/**
 * Information about total amount of CCD and the state of various administrative accounts.
 */
@Data
@Jacksonized
@Builder
public class RewardsOverview {

    /**
     * The amount in the baking reward account.
     */
    private final CCDAmount bakingRewardAccount;

    /**
     * The amount in the finalization reward account.
     */
    private final CCDAmount finalizationRewardAccount;

    /**
     * The transaction reward fraction accruing to the foundation (to be paid at next payday).
     */
    private final CCDAmount foundationTransactionRewards;

    /**
     * The amount in the GAS account.
     */
    private final CCDAmount gasAccount;

    /**
     * he rate at which CCD will be minted (as a proportion of the total supply) at the next payday
     */
    private final double nextPaydayMintRate;

    /**
     * The time of the next payday.
     */
    private final Date nextPaydayTime;

    /**
     * Protocol version that applies to these rewards. V0 variant only exists for protocol versions 1, 2, and 3.
     */
    private final ProtocolVersion protocolVersion;

    /**
     * The total CCD in existence.
     */
    private final CCDAmount totalAmount;

    /**
     * The total CCD in encrypted balances.
     */
    private final CCDAmount totalEncryptedAmount;

    /**
     * The total capital put up as stake by bakers and delegators
     */
    private final CCDAmount totalStakedCapital;

    public static Optional<RewardsOverview> fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            val ret = JsonMapper.INSTANCE.readValue(res.getValue(), RewardsOverview.class);
            return Optional.ofNullable(ret);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse RewardsOverview JSON", e);
        }
    }
}

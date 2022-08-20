package com.concordium.sdk.responses.rewardstatus;

import com.concordium.sdk.responses.ProtocolVersion;
import com.concordium.sdk.serializing.JsonMapper;
import com.concordium.sdk.transactions.CCDAmount;
import com.fasterxml.jackson.core.JsonProcessingException;
import concordium.ConcordiumP2PRpc;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

/**
 * Parsed Rewards Overview from response of
 * {@link concordium.P2PGrpc.P2PBlockingStub#getRewardStatus(ConcordiumP2PRpc.BlockHash)}
 */
@Data
@Getter
public class RewardsOverview {

    /**
     * The amount in the baking reward account.
     */
    private CCDAmount bakingRewardAccount;

    /**
     * The amount in the finalization reward account.
     */
    private CCDAmount finalizationRewardAccount;

    /**
     * The transaction reward fraction accruing to the foundation (to be paid at next payday).
     */
    private CCDAmount foundationTransactionRewards;

    /**
     * The amount in the GAS account.
     */
    private CCDAmount gasAccount;

    /**
     * he rate at which CCD will be minted (as a proportion of the total supply) at the next payday
     */
    private double nextPaydayMintRate;

    /**
     * The time of the next payday.
     */
    private Date nextPaydayTime;

    /**
     * Protocol version that applies to these rewards. V0 variant only exists for protocol versions 1, 2, and 3.
     */
    private ProtocolVersion protocolVersion;

    /**
     * The total CCD in existence.
     */
    private CCDAmount totalAmount;

    /**
     * The total CCD in encrypted balances.
     */
    private CCDAmount totalEncryptedAmount;

    /**
     * The total capital put up as stake by bakers and delegators
     */
    private CCDAmount totalStakedCapital;

    public static RewardsOverview fromJson(ConcordiumP2PRpc.JsonResponse res) {
        try {
            return JsonMapper.INSTANCE.readValue(res.getValue(), RewardsOverview.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot parse RewardsOverview JSON", e);
        }
    }
}

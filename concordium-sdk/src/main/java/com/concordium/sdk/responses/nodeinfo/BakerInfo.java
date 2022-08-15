package com.concordium.sdk.responses.nodeinfo;

import com.concordium.sdk.responses.AccountIndex;
import lombok.Getter;
import lombok.ToString;

/**
 * Baker information.
 */
@Getter
@ToString
public class BakerInfo {

    BakerInfo(AccountIndex bakerId, Status status, boolean isFinalizer) {
        this.bakerId = bakerId;
        this.status = status;
        this.isFinalizer = isFinalizer;
    }

    /**
     * The baker id
     */
    private final AccountIndex bakerId;

    /**
     * The status of the baker
     */
    private final Status status;

    /**
     * Whether the baker is also a finalizer.
     */
    private final boolean isFinalizer;

    /**
     * The status of the baker-configured node.
     */
    @ToString
    public enum Status {
        // The node is configured with baker keys but is not in the baking committee.
        NOT_IN_COMMITTEE,
        // The node has baker keys, but the account is not a baker for the current epoch.
        ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE,
        // The node has baker keys, but they don't match the current keys on the baker account.
        ADDED_BUT_WRONG_KEYS,
        // The node has valid baker keys and is active in the baker committee.
        ACTIVE_IN_COMMITTEE;

        static Status fromCode(int code) {
            switch (code) {
                case 0:
                    return NOT_IN_COMMITTEE;
                case 1:
                    return ADDED_BUT_NOT_ACTIVE_IN_COMMITTEE;
                case 2:
                    return ADDED_BUT_WRONG_KEYS;
                case 3:
                    return ACTIVE_IN_COMMITTEE;
                default:
                    throw new IllegalStateException("Unexpected baker info: " + code);
            }
        }
    }

}

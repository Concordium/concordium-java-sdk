package com.concordium.sdk.responses.accountinfo;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * The baker has removed its stake.
 */
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class RemoveStakeChange extends PendingChange {

}

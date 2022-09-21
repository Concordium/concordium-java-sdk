package com.concordium.sdk.exceptions;

import com.concordium.sdk.transactions.Hash;
import lombok.Getter;

/**
 * An exception thrown when a module was not found for a given block {@link Hash}
 */
public class ModuleNotFoundException extends Exception {
    @Getter
    private final Hash blockHash;
    @Getter
    private final Hash moduleRef;

    /**
     * Creates new {@link ModuleNotFoundException}.
     * This happens when a Module could not be found in a given block.
     * <p>
     * Use {@link ModuleNotFoundException#from(Hash, Hash)} to instantiate.
     *
     * @param blockHash Hex Encoded Block Hash.
     * @param moduleRef Hex Encoded Module Hash.
     */
    private ModuleNotFoundException(Hash blockHash, Hash moduleRef) {
        super("Module (" + moduleRef.toString() + ") not found for block " + blockHash.asHex());
        this.blockHash = blockHash;
        this.moduleRef = moduleRef;
    }

    public static ModuleNotFoundException from(Hash blockHash, Hash moduleRef) {
        return new ModuleNotFoundException(blockHash, moduleRef);
    }
}

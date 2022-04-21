package com.concordium.sdk;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BlocksAtHeightRequest {
    private final Type type;
    private final long height;

    private int genesisIndex;
    private boolean restrictedToGenesisIndex;

    private BlocksAtHeightRequest(Type type, long height) {
        this.type = type;
        this.height = height;
    }

    public BlocksAtHeightRequest(Type relative, long height, int genesisIndex, boolean restrictedToGenesisIndex) {
        this(relative, height);
        this.genesisIndex = genesisIndex;
        this.restrictedToGenesisIndex = restrictedToGenesisIndex;
    }

    public static BlocksAtHeightRequest newAbsolute(long height) {
        return new BlocksAtHeightRequest(Type.ABSOLUTE, height);
    }

    public static BlocksAtHeightRequest newRelative(long height, int genesisIndex, boolean restrictedToGenesisIndex) {
        return new BlocksAtHeightRequest(Type.RELATIVE, height, genesisIndex, restrictedToGenesisIndex);
    }

    public String toPrettyExceptionMessage() {
        switch (this.type) {
            case ABSOLUTE:
                return "Requested height: " + this.getHeight();
            case RELATIVE:
                return "Requested height: " + this.getHeight() + " genesis index " + this.getGenesisIndex() + " " + getMaybeRestrictedString();
            default:
                return "Invalid BlockHeightRequest.Type";
        }
    }

    private String getMaybeRestrictedString() {
        if (this.type == Type.RELATIVE) {
            return "(query was restricted to the genesis)";
        }
        return "";
    }


    public enum Type {
        ABSOLUTE,
        RELATIVE
    }
}

package com.concordium.sdk.types;

import java.util.ArrayList;
import java.util.List;

public class Map<T extends Tuple> extends ArrayList<T> {
    public Map(List<T> entries) {
        super(entries);
    }
}

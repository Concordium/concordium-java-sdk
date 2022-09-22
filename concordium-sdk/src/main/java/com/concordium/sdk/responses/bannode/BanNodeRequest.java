package com.concordium.sdk.responses.bannode;

import lombok.Getter;
import lombok.ToString;

import java.net.InetAddress;
import java.util.Optional;

@Getter
@ToString
public class BanNodeRequest {

    /**
     * Ip Address of Peer Node.
     */
    private final Optional<InetAddress> ip;

    /**
     * Hex Encoded ID of the Peer Node.
     */
    private final Optional<String> id;

    private BanNodeRequest(InetAddress ip) {
        this.ip = Optional.of(ip);
        this.id = Optional.empty();
    }

    private BanNodeRequest(String id) {
        this.ip = Optional.empty();
        this.id = Optional.of(id);
    }

    /**
     * Constructs {@link BanNodeRequest} from {@link InetAddress}.
     *
     * @param ip {@link InetAddress}.
     * @return {@link BanNodeRequest}.
     */
    public static BanNodeRequest from(InetAddress ip) {
        return new BanNodeRequest(ip);
    }

    /**
     * Constructs {@link BanNodeRequest} from Hex encoded Node Id.
     *
     * @param id Hex encoded Node Id.
     * @return {@link BanNodeRequest}.
     */
    public static BanNodeRequest from(String id) {
        return new BanNodeRequest(id);
    }
}

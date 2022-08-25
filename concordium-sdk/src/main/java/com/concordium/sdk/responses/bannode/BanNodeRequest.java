package com.concordium.sdk.responses.bannode;

import lombok.Getter;
import lombok.ToString;

import java.net.InetAddress;
import java.util.Objects;

@Getter
@ToString
public class BanNodeRequest {

    /**
     * Ip Address of Peer Node.
     */
    private InetAddress ip;

    /**
     * Hex Encoded ID of the Peer Node.
     */
    private String id;

    private BanNodeRequest(InetAddress ip) {
        this.ip = ip;
    }

    private BanNodeRequest(String id) {
        this.id = id;
    }

    /**
     * Is {@link BanNodeRequest#ip} initialized.
     *
     * @return true if {@link BanNodeRequest#ip} is initialized.
     */
    public boolean hasIp() {
        return !Objects.isNull(this.ip);
    }

    /**
     * Is {@link BanNodeRequest#id} initialized.
     *
     * @return true if {@link BanNodeRequest#id} is initialized.
     */
    public boolean hasId() {
        return !Objects.isNull(this.id);
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

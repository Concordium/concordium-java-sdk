package com.concordium.sdk.cis2;

import com.concordium.sdk.transactions.Parameter;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt16;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.Arrays;

import java.nio.ByteBuffer;
import java.util.Collection;

public class SerializationUtils {

    public static Parameter serializeBalanceOfParameter(Collection<BalanceQuery> query) {
        byte[] parameterBytes = UInt16.from(query.size()).getBytes();
        for (BalanceQuery balanceQuery : query) {
            val tokenIdBytes = SerializationUtils.serializeTokenId(balanceQuery.getTokenId());
            val addressBytes = SerializationUtils.serializeAddress(balanceQuery.getAddress());
            parameterBytes = Arrays.concatenate(parameterBytes, Arrays.concatenate(tokenIdBytes, addressBytes));
        }
        return Parameter.from(parameterBytes);
    }

    @SneakyThrows
    public static byte[] serializeTokenId(String hexTokenId) {
        byte[] tokenIdBytes = Hex.decodeHex(hexTokenId);
        // size of token + serialized token id.
        val buffer = ByteBuffer.allocate(1 + tokenIdBytes.length);
        buffer.put((byte) tokenIdBytes.length);
        if (tokenIdBytes.length != 0) {
            buffer.put(tokenIdBytes);
        }
        return buffer.array();
    }

    private static final int ACCOUNT_ADDRESS_SIZE = 1 + AccountAddress.BYTES;

    private static final int CONTRACT_ADDRESS_SIZE = 1 + (2 * 8);

    public static byte[] serializeAddress(AbstractAddress address) {
        if (address instanceof AccountAddress) {
            val accountAddress = (AccountAddress) address;
            val buffer = ByteBuffer.allocate(ACCOUNT_ADDRESS_SIZE);
            buffer.put((byte) 0); // tag
            buffer.put(accountAddress.getBytes());
            return buffer.array();
        } else if (address instanceof ContractAddress) {
            ContractAddress contractAddress = (ContractAddress) address;
            val buffer = ByteBuffer.allocate(CONTRACT_ADDRESS_SIZE);
            buffer.put((byte) 1); // tag
            buffer.putLong(contractAddress.getIndex());
            buffer.putLong(contractAddress.getSubIndex());
            return buffer.array();
        }
        throw new IllegalArgumentException("AbstractAddress must be either an account address or contract address");
    }
}

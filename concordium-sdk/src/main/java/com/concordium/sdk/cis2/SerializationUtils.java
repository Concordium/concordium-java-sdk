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

import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

class SerializationUtils {

    /**
     * Size of a serialized {@link AccountAddress}
     * Tag + the bytes of the account address.
     */
    private static final int ACCOUNT_ADDRESS_SIZE = 1 + AccountAddress.BYTES;

    /**
     * Size of a serialized {@link ContractAddress}
     * Tag + 8 bytes for the {@link ContractAddress#getIndex()} + 8 bytes for the {@link ContractAddress#getSubIndex()}.
     */
    private static final int CONTRACT_ADDRESS_SIZE = 1 + (2 * 8);

    static Parameter serializeBalanceOfParameter(Collection<BalanceQuery> queries) {
        // lengths are stored as little endian.
        byte[] parameterBytes = UInt16.from(queries.size()).getBytesLittleEndian();
        for (BalanceQuery balanceQuery : queries) {
            val tokenIdBytes = SerializationUtils.serializeTokenId(balanceQuery.getTokenId());
            val addressBytes = SerializationUtils.serializeAddress(balanceQuery.getAddress());
            parameterBytes = Arrays.concatenate(parameterBytes, Arrays.concatenate(tokenIdBytes, addressBytes));
        }
        return Parameter.from(parameterBytes);
    }

    static long[] deserializeTokenAmounts(byte[] returnValue) {
        val resultBuffer = ByteBuffer.wrap(returnValue);
        // lengths are stored as little endian.
        resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
        val noOfOutputs = UInt16.from(resultBuffer.getShort()).getValue();
        resultBuffer.order(ByteOrder.BIG_ENDIAN);
        val outputs = new long[noOfOutputs];
        for (int i = 0; i < noOfOutputs; i++) {
            outputs[i] = readUnsignedLeb128(resultBuffer);
        }
        return outputs;
    }

    static Parameter serializeOperatorOfParameter(Collection<OperatorQuery> queries) {
        // lengths are stored as little endian.
        byte[] parameterBytes = UInt16.from(queries.size()).getBytesLittleEndian();
        for (OperatorQuery operatorQuery : queries) {
            val ownerBytes = SerializationUtils.serializeAddress(operatorQuery.getOwner());
            val addressBytes = SerializationUtils.serializeAddress(operatorQuery.getAddress());
            parameterBytes = Arrays.concatenate(parameterBytes, Arrays.concatenate(ownerBytes, addressBytes));
        }
        return Parameter.from(parameterBytes);
    }

    static boolean[] deserializeOperatorOfResponse(byte[] returnValue) {
        val resultBuffer = ByteBuffer.wrap(returnValue);
        // lengths are stored as little endian.
        resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
        val noOfOutputs = UInt16.from(resultBuffer.getShort()).getValue();
        resultBuffer.order(ByteOrder.BIG_ENDIAN);
        val outputs = new boolean[noOfOutputs];
        for (int i = 0; i < noOfOutputs; i++) {
            outputs[i] = resultBuffer.get() != 0;
        }
        return outputs;
    }

    static Parameter serializeTokenIds(List<String> listOfQueries) {
        // lengths are stored as little endian.
        byte[] parameterBytes = UInt16.from(listOfQueries.size()).getBytesLittleEndian();
        for (String tokenId : listOfQueries) {
            val tokenIdBytes = SerializationUtils.serializeTokenId(tokenId);
            parameterBytes = Arrays.concatenate(parameterBytes, tokenIdBytes);
        }
        return Parameter.from(parameterBytes);
    }

    @SneakyThrows
    static MetadataResponse[] deserializeTokenMetadatas(byte[] returnValue) {
        val resultBuffer = ByteBuffer.wrap(returnValue);
        // lengths are stored as little endian.
        resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
        val noOfOutputs = UInt16.from(resultBuffer.getShort()).getValue();
        val outputs = new MetadataResponse[noOfOutputs];
        for (int i = 0; i < noOfOutputs; i++) {
            val urlLength = UInt16.from(resultBuffer.getShort()).getValue();
            resultBuffer.order(ByteOrder.BIG_ENDIAN);
            val urlBytes = new byte[urlLength];
            resultBuffer.get(urlBytes);
            val hasChecksum = resultBuffer.get() != 0;
            byte[] checksumBuffer = null;
            if (hasChecksum) {
                checksumBuffer = new byte[32];
                resultBuffer.get(checksumBuffer);
            }
            outputs[i] = new MetadataResponse(new URL(new String(urlBytes, StandardCharsets.UTF_8)), checksumBuffer);
            resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return outputs;
    }

    @SneakyThrows
    static byte[] serializeTokenId(String hexTokenId) {
        byte[] tokenIdBytes = Hex.decodeHex(hexTokenId);
        // size of token + serialized token id.
        val buffer = ByteBuffer.allocate(1 + tokenIdBytes.length);
        buffer.put((byte) tokenIdBytes.length);
        if (tokenIdBytes.length != 0) {
            buffer.put(tokenIdBytes);
        }
        return buffer.array();
    }

    static byte[] serializeAddress(AbstractAddress address) {
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
            // index and sub-index are stored as little endian.
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putLong(contractAddress.getIndex());
            buffer.putLong(contractAddress.getSubIndex());
            return buffer.array();
        }
        throw new IllegalArgumentException("AbstractAddress must be either an account address or contract address");
    }

    private static int readUnsignedLeb128(ByteBuffer buffer) {
        int result = 0;
        int cur;
        int count = 0;
        do {
            cur = buffer.get() & 0xff;
            result |= (cur & 0x7f) << (count * 7);
            count++;
        } while (((cur & 0x80) == 0x80) && count < 5);
        if ((cur & 0x80) == 0x80) {
            throw new IllegalArgumentException("invalid LEB128 encoding");
        }
        return result;
    }

    public static Parameter serializeUpdateOperators(Map<AbstractAddress, Boolean> operatorUpdates) {
        return null;
    }
}

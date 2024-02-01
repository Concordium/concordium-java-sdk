package com.concordium.sdk.cis2;

import com.concordium.sdk.transactions.Parameter;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt16;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    @SneakyThrows
    public static Parameter serializeTransfers(List<Cis2Transfer> listOfTransfers) {
        val bos = new ByteArrayOutputStream();
        bos.write(UInt16.from(listOfTransfers.size()).getBytesLittleEndian());
        for (Cis2Transfer transfer : listOfTransfers) {
            bos.write(SerializationUtils.serializeTokenId(transfer.getHexEncodedTokenId()));
            writeUnsignedLeb128(bos, transfer.getTokenAmount());
            bos.write(SerializationUtils.serializeAddress(transfer.getSender()));
            bos.write(SerializationUtils.serializeAddress(transfer.getReceiver()));
            bos.write(transfer.getAdditionalData());
        }
        return Parameter.from(bos.toByteArray());
    }

    @SneakyThrows
    static Parameter serializeBalanceOfParameter(Collection<BalanceQuery> queries) {
        val bos = new ByteArrayOutputStream();
        // lengths are stored as little endian.
        bos.write(UInt16.from(queries.size()).getBytesLittleEndian());
        for (BalanceQuery balanceQuery : queries) {
            bos.write(SerializationUtils.serializeTokenId(balanceQuery.getTokenId()));
            bos.write(SerializationUtils.serializeAddress(balanceQuery.getAddress()));
        }
        return Parameter.from(bos.toByteArray());
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

    @SneakyThrows
    static Parameter serializeOperatorOfParameter(Collection<OperatorQuery> queries) {
        val bos = new ByteArrayOutputStream();
        // lengths are stored as little endian.
        bos.write(UInt16.from(queries.size()).getBytesLittleEndian());
        for (OperatorQuery operatorQuery : queries) {
            bos.write(SerializationUtils.serializeAddress(operatorQuery.getOwner()));
            bos.write(SerializationUtils.serializeAddress(operatorQuery.getAddress()));
        }
        return Parameter.from(bos.toByteArray());
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

    @SneakyThrows
    static Parameter serializeTokenIds(List<String> listOfQueries) {
        val bos = new ByteArrayOutputStream();
        // lengths are stored as little endian.
        bos.write(UInt16.from(listOfQueries.size()).getBytesLittleEndian());
        for (String tokenId : listOfQueries) {
            bos.write(SerializationUtils.serializeTokenId(tokenId));
        }
        return Parameter.from(bos.toByteArray());
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

    @SneakyThrows
    public static Parameter serializeUpdateOperators(Map<AbstractAddress, Boolean> operatorUpdates) {
        val bos = new ByteArrayOutputStream();
        bos.write(UInt16.from(operatorUpdates.size()).getBytesLittleEndian());
        for (AbstractAddress address : operatorUpdates.keySet()) {
            bos.write((byte) (operatorUpdates.get(address) ? 1 : 0));
            bos.write(SerializationUtils.serializeAddress(address));
        }
        return Parameter.from(bos.toByteArray());
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

    private static void writeUnsignedLeb128(ByteArrayOutputStream bos, int value) {
        int remaining = value >>> 7;
        while (remaining != 0) {
            bos.write((byte) ((value & 0x7f) | 0x80));
            value = remaining;
            remaining >>>= 7;
        }
        bos.write((byte) (value & 0x7f));
    }
}

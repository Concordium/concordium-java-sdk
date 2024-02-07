package com.concordium.sdk.cis2;

import com.concordium.sdk.cis2.events.*;
import com.concordium.sdk.transactions.Parameter;
import com.concordium.sdk.types.AbstractAddress;
import com.concordium.sdk.types.AccountAddress;
import com.concordium.sdk.types.ContractAddress;
import com.concordium.sdk.types.UInt16;
import lombok.SneakyThrows;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SerializationUtils {

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
            bos.write(SerializationUtils.serializeTokenId(transfer.getTokenId()));
            writeUnsignedLeb128(bos, transfer.getTokenAmount());
            bos.write(SerializationUtils.serializeAddress(transfer.getSender()));
            bos.write(SerializationUtils.serializeAddress(transfer.getReceiver()));
            if (Objects.isNull(transfer.getAdditionalData()) || transfer.getAdditionalData().length == 0) {
                bos.write(UInt16.from(0).getBytesLittleEndian());
            } else {
                bos.write(UInt16.from(transfer.getAdditionalData().length).getBytesLittleEndian());
                bos.write(transfer.getAdditionalData());
            }
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
    static Parameter serializeTokenIds(List<TokenId> listOfQueries) {
        val bos = new ByteArrayOutputStream();
        // lengths are stored as little endian.
        bos.write(UInt16.from(listOfQueries.size()).getBytesLittleEndian());
        for (TokenId tokenId : listOfQueries) {
            bos.write(SerializationUtils.serializeTokenId(tokenId));
        }
        return Parameter.from(bos.toByteArray());
    }

    @SneakyThrows
    static TokenMetadata[] deserializeTokenMetadatas(byte[] returnValue) {
        val resultBuffer = ByteBuffer.wrap(returnValue);
        // lengths are stored as little endian.
        resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
        val noOfOutputs = UInt16.from(resultBuffer.getShort()).getValue();
        val outputs = new TokenMetadata[noOfOutputs];
        for (int i = 0; i < noOfOutputs; i++) {
            outputs[i] = deserializeTokenMetadata(resultBuffer);
            resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        return outputs;
    }

    private static TokenMetadata deserializeTokenMetadata(ByteBuffer resultBuffer) throws MalformedURLException {
        resultBuffer.order(ByteOrder.LITTLE_ENDIAN);
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
        return new TokenMetadata(new URL(new String(urlBytes, StandardCharsets.UTF_8)), checksumBuffer);
    }

    @SneakyThrows
    static byte[] serializeTokenId(TokenId tokenId) {
        // size of token + serialized token id.
        val buffer = ByteBuffer.allocate(1 + tokenId.getSize());
        buffer.put((byte) tokenId.getSize());
        if (tokenId.getSize() != 0) {
            buffer.put(tokenId.getBytes());
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

    public static Cis2Event deserializeCis2Event(byte[] eventBytes) {
        val buffer = ByteBuffer.wrap(eventBytes);
        val tag = buffer.get();
        val eventType = Cis2Event.Type.parse(tag);
        switch (eventType) {
            case TRANSFER:
                return SerializationUtils.deserializeTransferEvent(buffer);
            case MINT:
                return SerializationUtils.deserializeMintEvent(buffer);
            case BURN:
                return SerializationUtils.deserializeBurnEvent(buffer);
            case UPDATE_OPERATOR_OF:
                return SerializationUtils.deserializeUpdateOperatorOfEvent(buffer);
            case TOKEN_METADATA:
                return SerializationUtils.deserializeTokenMetadataEvent(buffer);
            case CUSTOM:
                return SerializationUtils.deserializeCustomEvent(tag, buffer);
        }
        throw new IllegalArgumentException("Malformed CIS2 event");
    }

    private static Cis2Event deserializeCustomEvent(byte tag, ByteBuffer buffer) {
        return new CustomEvent(tag, buffer.array());
    }

    @SneakyThrows
    private static Cis2Event deserializeTokenMetadataEvent(ByteBuffer buffer) {
        val tokenId = deserializeTokenId(buffer);
        val tokenMetadata = deserializeTokenMetadata(buffer);
        return new TokenMetadataEvent(tokenId, tokenMetadata);
    }

    private static Cis2Event deserializeUpdateOperatorOfEvent(ByteBuffer buffer) {
        val isOperator = buffer.get() != 0;
        val owner = deserializeAddress(buffer);
        val operator = deserializeAddress(buffer);
        return new UpdateOperatorEvent(isOperator, owner, operator);
    }

    private static Cis2Event deserializeBurnEvent(ByteBuffer buffer) {
        val tokenId = deserializeTokenId(buffer);
        val tokenAmount = readUnsignedLeb128(buffer);
        val owner = deserializeAddress(buffer);
        return new BurnEvent(tokenId, tokenAmount, owner);
    }

    private static Cis2Event deserializeMintEvent(ByteBuffer buffer) {
        val tokenId = deserializeTokenId(buffer);
        val tokenAmount = readUnsignedLeb128(buffer);
        val owner = deserializeAddress(buffer);
        return new MintEvent(tokenId, tokenAmount, owner);
    }

    public static Cis2Event deserializeTransferEvent(ByteBuffer buffer) {
        val tokenId = deserializeTokenId(buffer);
        val tokenAmount = readUnsignedLeb128(buffer);
        val from = deserializeAddress(buffer);
        val to = deserializeAddress(buffer);
        return new TransferEvent(tokenId, tokenAmount, from, to);
    }

    @SneakyThrows
    static TokenId deserializeTokenId(ByteBuffer buffer) {
        byte tokenLength = buffer.get();
        val tokenBuffer = new byte[tokenLength];
        buffer.get(tokenBuffer);
        return TokenId.from(tokenBuffer);
    }

    static AbstractAddress deserializeAddress(ByteBuffer buffer) {
        byte tag = buffer.get();
        if (tag == 0) {
            val addressBuffer = new byte[AccountAddress.BYTES];
            buffer.get(addressBuffer);
            return AccountAddress.from(addressBuffer);
        }
        if (tag == 1) {
            long index = buffer.getLong();
            long subIndex = buffer.getLong();
            return ContractAddress.from(index, subIndex);
        }
        throw new IllegalArgumentException("Malformed address");
    }
}

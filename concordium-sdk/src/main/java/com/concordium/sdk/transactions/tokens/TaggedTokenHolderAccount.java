package com.concordium.sdk.transactions.tokens;

import com.concordium.sdk.types.AccountAddress;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.Getter;
import lombok.val;

import java.io.IOException;

@Getter
@JsonSerialize(using = TaggedTokenHolderAccount.CborSerializer.class)
public class TaggedTokenHolderAccount {

    private final byte[] data;

    public TaggedTokenHolderAccount(AccountAddress accountAddress) {
        this.data = accountAddress.getBytes();
    }

    static class CborSerializer extends JsonSerializer<TaggedTokenHolderAccount> {

        @Override
        public void serialize(TaggedTokenHolderAccount taggedTokenHolderAccount,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            val cborGenerator = (CBORGenerator) jsonGenerator;

            cborGenerator.writeTag(40307);

            if (taggedTokenHolderAccount == null) {
                cborGenerator.writeNull();
                return;
            }

            cborGenerator.writeStartObject(taggedTokenHolderAccount, 1);

            cborGenerator.writeFieldId(3);
            cborGenerator.writeObject(taggedTokenHolderAccount.data);

            cborGenerator.writeEndObject();
        }
    }
}

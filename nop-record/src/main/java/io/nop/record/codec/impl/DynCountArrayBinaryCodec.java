package io.nop.record.codec.impl;

import io.nop.record.codec.IFieldBinaryCodec;
import io.nop.record.codec.IFieldCodecContext;
import io.nop.record.reader.IBinaryDataReader;
import io.nop.record.serialization.IModelBasedBinaryRecordDeserializer;
import io.nop.record.serialization.IModelBasedBinaryRecordSerializer;
import io.nop.record.writer.IBinaryDataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DynCountArrayBinaryCodec implements IFieldBinaryCodec {
    private final IFieldBinaryCodec countCodec;

    private final int itemLength;

    public DynCountArrayBinaryCodec(IFieldBinaryCodec countCodec, int itemLength) {
        this.countCodec = countCodec;
        this.itemLength = itemLength;
    }

    @Override
    public Object decode(IBinaryDataReader input, Object record, int length, IFieldCodecContext context,
                         IModelBasedBinaryRecordDeserializer deserializer) throws IOException {
        int count = (Integer) countCodec.decode(input, record, length, context, deserializer);

        IBinaryDataReader arrayInput = input.subInput(length);

        List<Object> ret = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Object item = null; // itemCodec.decode(arrayInput, itemLength, charset, context);
            ret.add(item);
        }
        return ret;
    }

    @Override
    public void encode(IBinaryDataWriter output, Object value, int length,
                       IFieldCodecContext context, IModelBasedBinaryRecordSerializer serializer) throws IOException {
        Collection<Object> list = (Collection<Object>) value;
        if (list == null)
            list = Collections.emptyList();

        countCodec.encode(output, list.size(), -1, context, null);
        for (Object item : list) {
            //serializer.writeObject(output, item, itemLength, context, serializer);
        }
    }
}

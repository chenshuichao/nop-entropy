package io.nop.record.codec.impl;

import io.nop.core.lang.eval.DisabledEvalScope;
import io.nop.core.reflect.IPropertyGetter;
import io.nop.core.reflect.IPropertySetter;
import io.nop.core.reflect.bean.IBeanConstructor;
import io.nop.record.codec.IFieldBinaryCodec;
import io.nop.record.codec.IFieldCodecContext;
import io.nop.record.reader.IBinaryDataReader;
import io.nop.record.serialization.IModelBasedBinaryRecordDeserializer;
import io.nop.record.serialization.IModelBasedBinaryRecordSerializer;
import io.nop.record.writer.IBinaryDataWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class BeanBinaryCodec implements IFieldBinaryCodec {
    public static class PropCodec {
        private final String name;
        private final IFieldBinaryCodec codec;
        private final int length;
        private final Charset charset;

        private final IPropertySetter setter;

        private final IPropertyGetter getter;

        public PropCodec(String name, IPropertySetter setter, IPropertyGetter getter,
                         IFieldBinaryCodec codec, int length, Charset charset) {
            this.name = name;
            this.setter = setter;
            this.getter = getter;
            this.codec = codec;
            this.length = length;
            this.charset = charset;
        }
    }

    private final IBeanConstructor constructor;
    private final List<PropCodec> props;

    public BeanBinaryCodec(IBeanConstructor constructor,
                           List<PropCodec> props) {
        this.constructor = constructor;
        this.props = props;
    }

    @Override
    public Object decode(IBinaryDataReader input, Object record, int length, IFieldCodecContext context,
                         IModelBasedBinaryRecordDeserializer deserializer) throws IOException {
        Object bean = constructor.newInstance();
        for (PropCodec prop : props) {
            Object value = prop.codec.decode(input, record, prop.length, context, deserializer);
            prop.setter.setProperty(bean, prop.name, value, DisabledEvalScope.INSTANCE);
        }
        return bean;
    }

    @Override
    public void encode(IBinaryDataWriter output, Object bean, int length,
                       IFieldCodecContext context,
                       IModelBasedBinaryRecordSerializer serializer) throws IOException {
        for (PropCodec prop : props) {
            Object value = prop.getter.getProperty(bean, prop.name, DisabledEvalScope.INSTANCE);
            prop.codec.encode(output, value, prop.length, context, null);
        }
    }
}

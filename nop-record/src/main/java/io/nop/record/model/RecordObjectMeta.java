/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.record.model;

import io.nop.api.core.exceptions.NopException;
import io.nop.commons.bytes.ByteString;
import io.nop.commons.text.SimpleTextTemplate;
import io.nop.core.reflect.ReflectionManager;
import io.nop.core.reflect.bean.IBeanConstructor;
import io.nop.core.reflect.bean.MethodBeanConstructor;
import io.nop.record.codec.IFieldTagBinaryCodec;
import io.nop.record.codec.IFieldTagTextCodec;
import io.nop.record.model._gen._RecordObjectMeta;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

import static io.nop.record.RecordErrors.ARG_FIELD_NAME;
import static io.nop.record.RecordErrors.ERR_RECORD_UNKNOWN_FIELD;

public class RecordObjectMeta extends _RecordObjectMeta implements IRecordFieldsMeta {
    private String name;
    private SimpleTextTemplate normalizedTemplate;

    private IBeanConstructor constructor;

    private Charset charsetObj;

    private RecordObjectMeta resolvedBaseType;

    private IFieldTagBinaryCodec resolvedTagBinaryCodec;
    private IFieldTagTextCodec resolvedTagTextCodec;

    private int totalLength;

    public RecordObjectMeta() {

    }

    /**
     * 所有子字段的长度加在一起
     */
    public int getTotalLength() {
        if (totalLength == 0) {
            int total = 0;
            List<RecordFieldMeta> fields = getFields();
            for (RecordFieldMeta field : fields) {
                total += field.getLength();
            }
            totalLength = total;
        }
        return totalLength;
    }

    @Override
    public IFieldTagBinaryCodec getResolvedTagBinaryCodec() {
        return resolvedTagBinaryCodec;
    }

    @Override
    public void setResolvedTagBinaryCodec(IFieldTagBinaryCodec resolvedTagBinaryCodec) {
        this.resolvedTagBinaryCodec = resolvedTagBinaryCodec;
    }

    @Override
    public IFieldTagTextCodec getResolvedTagTextCodec() {
        return resolvedTagTextCodec;
    }

    @Override
    public void setResolvedTagTextCodec(IFieldTagTextCodec resolvedTagTextCodec) {
        this.resolvedTagTextCodec = resolvedTagTextCodec;
    }

    public boolean hasFieldsOrTemplate() {
        return !getFields().isEmpty() || getTemplate() != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Charset getCharsetObj() {
        return charsetObj;
    }

    public RecordObjectMeta getResolvedBaseType() {
        return resolvedBaseType;
    }

    public void setResolvedBaseType(RecordObjectMeta resolvedBaseType) {
        this.resolvedBaseType = resolvedBaseType;
    }

    public void init(RecordDefinitions defs) {
        if (getTemplate() != null) {
            this.normalizedTemplate = SimpleTextTemplate.normalize(getTemplate());
        }

        for (RecordFieldMeta field : getFields()) {
            field.init(defs);
        }

        if (!getFields().isEmpty()) {
            getFields().get(0).setFirstField(true);
            getFields().get(getFields().size() - 1).setLastField(true);
        }

        this.charsetObj = defs.getDefaultCharsetObj();

        if (getBeanClass() != null) {
            constructor = new MethodBeanConstructor(ReflectionManager.instance().loadClassModel(getBeanClass()).getConstructor(0));
        } else {
            constructor = LinkedHashMap::new;
        }

        if (getBaseType() != null) {
            resolvedBaseType = defs.resolveType(getBaseType());
        }
    }

    public Object newBean() {
        return constructor.newInstance();
    }

    @Override
    public SimpleTextTemplate getNormalizedTemplate() {
        return this.normalizedTemplate;
    }


    public ByteString getPrefix() {
        if (getNormalizedTemplate() != null)
            return getNormalizedTemplate().getPrefix();

        List<RecordFieldMeta> fields = getFields();
        if (fields.isEmpty())
            return null;
        RecordFieldMeta field = fields.get(0);
        return field.getContent();
    }

    @Override
    public RecordFieldMeta requireField(String fieldName) {
        RecordFieldMeta field = getField(fieldName);
        if (field == null)
            throw new NopException(ERR_RECORD_UNKNOWN_FIELD)
                    .param(ARG_FIELD_NAME, fieldName);
        return field;
    }
}
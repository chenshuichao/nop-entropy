/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.rpc.model.proto;

import io.nop.api.core.beans.DictBean;
import io.nop.api.core.beans.DictOptionBean;
import io.nop.commons.text.CodeBuilder;
import io.nop.commons.util.StringHelper;
import io.nop.core.lang.json.JsonTool;
import io.nop.core.resource.IResource;
import io.nop.core.resource.ResourceHelper;
import io.nop.core.type.IGenericType;
import io.nop.rpc.model.ApiImportModel;
import io.nop.rpc.model.ApiMessageFieldModel;
import io.nop.rpc.model.ApiMessageModel;
import io.nop.rpc.model.ApiMethodModel;
import io.nop.rpc.model.ApiModel;
import io.nop.rpc.model.ApiOptionModel;
import io.nop.rpc.model.ApiServiceModel;
import io.nop.rpc.model.IWithOptions;

public class ProtoFileGenerator extends CodeBuilder {
    public ProtoFileGenerator(Appendable out, int lineLength) {
        super(out, lineLength);
    }

    public ProtoFileGenerator(int lineLength) {
        super(lineLength);
    }

    public ProtoFileGenerator() {
        this(1024);
    }

    public void generateToResource(ApiModel model, IResource resource) {
        ResourceHelper.writeText(resource, generateProtoFile(model));
    }

    public String generateProtoFile(ApiModel model) {
        append("syntax = \"proto3\";").line();
        line();

        printOptions(model);

        String apiPackageName = model.getApiPackageName();
        if (StringHelper.isEmpty(apiPackageName))
            apiPackageName = "app";

        printDescription(model.getDescription());

        append("package ").append(apiPackageName).append(";").line();

        if (model.hasImports()) {
            printImports(model);
            line();
        }

        if (model.hasDicts()) {
            printEnums(model);
            line();
        }

        if (model.hasMessages()) {
            printMessages(model);
            line();
        }

        printServices(model);
        return this.toString();
    }

    void printDescAndOptions(IWithOptions model) {
        printDescription(model.getDescription());
        incIndent();
        printOptions(model);
        decIndent();
    }

    void printOptions(IWithOptions model) {
        for (ApiOptionModel optionModel : model.getOptions()) {
            printOption(optionModel);
        }
    }

    void printOption(ApiOptionModel optionModel) {
        printDescription(optionModel.getDescription());
        indent();
        append("option ").append(optionModel.getName()).append(" = ");
        append(JsonTool.stringify(optionModel.getValue()));
        append(';');
    }

    void printImports(ApiModel model) {
        for (ApiImportModel importModel : model.getImports()) {
            printImport(importModel);
        }
    }

    void printImport(ApiImportModel importModel) {
        indent();
        append("import ").append(StringHelper.quote(importModel.getFrom())).append(";");
    }

    void printEnums(ApiModel model) {
        for (DictBean enumModel : model.getDicts()) {
            printDict(enumModel);
        }
    }

    void printDescription(String desc) {
        if (!StringHelper.isEmpty(desc)) {
            indent();
            append("/* ");
            append(StringHelper.replace(desc, "*/", "* /"));
            append("*/");
        }
    }

    void printDict(DictBean enumModel) {
        printDescription(enumModel.getDescription());

        indent();
        append("enum ").append(enumModel.getName()).append(" {").line();
        incIndent();
        for (DictOptionBean item : enumModel.getOptions()) {
            line();
            indent();
            append(item.getLabel()).append(" = ").append(String.valueOf(item.getValue())).append(';');
        }
        decIndent();
        line();
        indent();
        append("}").line();
    }

    void printMessages(ApiModel model) {
        for (ApiMessageModel messageModel : model.getMessages()) {
            printMessage(messageModel);
        }
    }

    void printMessage(ApiMessageModel messageModel) {
        printDescAndOptions(messageModel);

        indent();
        append("message ").append(messageModel.getName()).append(" {");
        incIndent();
        for (ApiMessageFieldModel fieldModel : messageModel.getFields()) {
            line();
            printField(fieldModel);
        }
        decIndent();
        line();
        indent();
        append("}").line();
    }

    void printField(ApiMessageFieldModel fieldModel) {
        printDescAndOptions(fieldModel);

        indent();

        if (fieldModel.getBinaryScalarType() != null) {
            if (!fieldModel.isMandatory()) {
                append("optional ");
            }
            append(fieldModel.getBinaryScalarType().toProtoBufTypeName()).append(' ').append(fieldModel.getName());
            append(" = ").append(String.valueOf(fieldModel.getPropId())).append(';');
        } else {
            IGenericType type = fieldModel.getType();
            if (type == null) {
                append("Any").append(' ').append(fieldModel.getName());
                append(" = ").append(String.valueOf(fieldModel.getPropId())).append(';');
            } else {
                if (type.isCollectionLike()) {
                    append("repeated ");
                    type = type.getComponentType();
                } else if (!fieldModel.isMandatory()) {
                    append("optional ");
                }

                append(getProtoTypeName(type)).append(' ').append(fieldModel.getName());
                append(" = ").append(String.valueOf(fieldModel.getPropId())).append(';');
            }
        }
    }

    String getProtoTypeName(IGenericType type) {
        return ProtoHelper.getProtoTypeName(type);
    }

    void printServices(ApiModel model) {
        for (ApiServiceModel serviceModel : model.getServices()) {
            printService(serviceModel);
        }
    }

    void printService(ApiServiceModel serviceModel) {
        printDescAndOptions(serviceModel);

        indent();
        append("service ").append(serviceModel.getName()).append(" {");
        incIndent();
        for (ApiMethodModel methodModel : serviceModel.getMethods()) {
            line();
            printMethod(serviceModel.getName() + "__" + methodModel.getName(), methodModel);
        }
        decIndent();
        line();
        indent();
        append("}").append('\n');
    }

    void printMethod(String fullMethodName, ApiMethodModel methodModel) {
        printDescAndOptions(methodModel);

        indent();
        append("rpc ").append(methodModel.getName());
        append('(');
        append(getProtoBufType(methodModel.getRequestMessage()));
        append(')').append(' ');
        append("returns (");
        append(getResponseTypeName(fullMethodName, methodModel.getResponseMessage()));
        append(");");
    }

    private String getProtoBufType(String typeName) {
        return ProtoHelper.getRequestProtoTypeName(typeName);
    }

    private String getResponseTypeName(String fullMethodName, IGenericType type) {
        return ProtoHelper.getResponseProtoTypeName(fullMethodName, type);
    }
}

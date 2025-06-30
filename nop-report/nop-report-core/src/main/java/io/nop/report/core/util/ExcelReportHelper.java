/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.report.core.util;

import io.nop.api.core.beans.WebContentBean;
import io.nop.api.core.exceptions.NopException;
import io.nop.commons.concurrent.executor.GlobalExecutors;
import io.nop.commons.util.StringHelper;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.resource.IResource;
import io.nop.core.resource.ResourceHelper;
import io.nop.core.resource.VirtualFileSystem;
import io.nop.core.resource.component.ResourceComponentManager;
import io.nop.core.resource.tpl.IBinaryTemplateOutput;
import io.nop.core.resource.tpl.ITemplateOutput;
import io.nop.core.resource.tpl.ITextTemplateOutput;
import io.nop.excel.imp.model.ImportModel;
import io.nop.excel.model.ExcelWorkbook;
import io.nop.ooxml.xlsx.parse.ExcelWorkbookParser;
import io.nop.ooxml.xlsx.util.ExcelHelper;
import io.nop.report.core.XptConstants;
import io.nop.report.core.build.XptModelInitializer;
import io.nop.report.core.engine.ExpandedSheetGenerator;
import io.nop.report.core.engine.IReportEngine;
import io.nop.report.core.engine.renderer.HtmlReportRendererFactory;
import io.nop.report.core.engine.renderer.ReportRenderHelper;
import io.nop.report.core.engine.renderer.XlsxReportRendererFactory;
import io.nop.report.core.imp.ExcelTemplateToXptModelTransformer;
import io.nop.xlang.api.XLang;
import io.nop.xlang.api.XLangCompileTool;
import io.nop.xlang.xdsl.DslModelHelper;

import java.util.concurrent.TimeUnit;

public class ExcelReportHelper extends ExcelHelper {

    public static void saveXlsxObject(String impModelPath, IResource resource, Object obj) {
        saveXlsxObject(impModelPath, resource, obj, XLang.newEvalScope());
    }

    public static void saveXlsxObject(String impModelPath, IResource resource, Object obj, IEvalScope scope) {
        ExcelWorkbook xptModel = buildXptModelFromImpModel(impModelPath);

        scope.setLocalValue(null, XptConstants.VAR_ENTITY, obj);

        IBinaryTemplateOutput output = new XlsxReportRendererFactory()
                .buildRenderer(xptModel, new ExpandedSheetGenerator(xptModel));
        output.generateToResource(resource, scope);
    }

    public static void saveXlsxObject(IReportEngine reportEngine, String impModelPath, IResource resource,
                                      Object obj, String renderType, IEvalScope scope) {
        scope.setLocalValue(null, XptConstants.VAR_ENTITY, obj);

        ExcelWorkbook workbook = reportEngine.buildXptModelFromImpModel(impModelPath);
        ITemplateOutput output = reportEngine.getRendererForXptModel(workbook, renderType);
        output.generateToResource(resource, scope);
    }

    public static ExcelWorkbook generateExcelWorkbook(String impModelPath, Object obj) {
        return generateExcelWorkbook(impModelPath, obj, XLang.newEvalScope());
    }

    public static ExcelWorkbook generateExcelWorkbook(String impModelPath, Object obj, IEvalScope scope) {
        ExcelWorkbook xptModel = buildXptModelFromImpModel(impModelPath);

        scope.setLocalValue(null, XptConstants.VAR_ENTITY, obj);
        return ReportRenderHelper.renderModel(xptModel, new ExpandedSheetGenerator(xptModel), scope);
    }

    public static String getHtmlForXlsxObject(String impModelPath, Object obj) {
        return getHtmlForXlsxObject(impModelPath, obj, XLang.newEvalScope());
    }

    public static String getHtmlForXlsxObject(String impModelPath, Object obj, IEvalScope scope) {
        scope.setLocalValue(null, XptConstants.VAR_ENTITY, obj);

        ExcelWorkbook workbook = buildXptModelFromImpModel(impModelPath);
        ITextTemplateOutput output = new HtmlReportRendererFactory().buildRenderer(workbook, new ExpandedSheetGenerator(workbook));
        return output.generateText(scope);
    }

    public static ExcelWorkbook buildXptModelFromImpModel(String impModelPath) {
        ImportModel impModel = (ImportModel) ResourceComponentManager.instance().loadComponentModel(impModelPath);

        IResource resource = VirtualFileSystem.instance().getResource(impModel.getTemplatePath());
        ExcelWorkbook template = new ExcelWorkbookParser().parseFromResource(resource);

        new ExcelTemplateToXptModelTransformer(XLang.newEvalScope()).transform(template, impModel);

        XLangCompileTool cp = XLang.newCompileTool().allowUnregisteredScopeVar(true);
        new XptModelInitializer(cp).initialize(template);

        return template;
    }

    public static void dumpXptModel(ExcelWorkbook workbook) {
        String path = workbook.resourcePath();
        if (StringHelper.isEmpty(path))
            path = "unknown.xpt.xlsx";
        path = StringHelper.removeTail(path, ".xlsx");
        if (!path.endsWith(".xpt"))
            path += ".xpt";

        path = ResourceHelper.getDumpPath(path);

        IResource resource = VirtualFileSystem.instance().getResource(path);
        DslModelHelper.saveDslModel(XptConstants.XDSL_SCHEMA_WORKBOOK, workbook, resource);
    }

    public static WebContentBean downloadXlsx(String fileName, String impModelPath, Object bean, int waitMinutes) {
        IResource resource = ResourceHelper.getTempResource("download");
        try {
            ExcelReportHelper.saveXlsxObject(impModelPath, resource, bean);

            WebContentBean content = new WebContentBean("application/octet-stream",
                    resource.toFile(), fileName);

            GlobalExecutors.globalTimer().schedule(() -> {
                resource.delete();
                return null;
            }, waitMinutes, TimeUnit.MINUTES);

            return content;
        } catch (Exception e) {
            resource.delete();
            throw NopException.adapt(e);
        }
    }
}
/*
 * Copyright (c) 2017-2025 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */

package io.nop.idea.plugin.lang;

import com.intellij.codeInsight.documentation.DocumentationManagerProtocol;
import com.intellij.codeInsight.javadoc.JavaDocHighlightingManagerImpl;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lang.documentation.DocumentationSettings;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil;
import com.intellij.openapi.util.text.HtmlChunk;
import com.intellij.psi.PsiElement;
import io.nop.api.core.util.ISourceLocationGetter;
import io.nop.commons.util.StringHelper;
import io.nop.idea.plugin.messages.NopPluginBundle;
import io.nop.idea.plugin.utils.MarkdownHelper;
import io.nop.idea.plugin.utils.XmlPsiHelper;
import io.nop.idea.plugin.vfs.NopVirtualFile;
import io.nop.xlang.xdef.IXDefAttribute;
import io.nop.xlang.xdef.IXDefNode;
import io.nop.xlang.xdef.XDefTypeDecl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * XLang 说明文档
 *
 * @author <a href="mailto:flytreeleft@crazydan.org">flytreeleft</a>
 * @date 2025-07-18
 */
public class XLangDocumentation {
    private static final String LINK_PREFIX_NOP_VFS = "nop-vfs:";

    String mainTitle;
    String subTitle;
    String stdDomain;
    Boolean required;
    String desc;
    String path;

    public XLangDocumentation(ISourceLocationGetter locGetter) {
        this(locGetter, null);
    }

    public XLangDocumentation(IXDefNode defNode) {
        this(defNode, defNode.getXdefValue());
    }

    public XLangDocumentation(IXDefAttribute attrDef) {
        this(attrDef, attrDef.getType());
    }

    XLangDocumentation(ISourceLocationGetter locGetter, XDefTypeDecl type) {
        this.path = XmlPsiHelper.getNopVfsPath(locGetter);

        if (type != null) {
            this.required = type.isMandatory();
            this.stdDomain = type.getStdDomain();
            if (type.getOptions() != null) {
                this.stdDomain += ':' + type.getOptions();
            }
        }
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String genDoc() {
        JavaDocHighlightingManagerImpl highlightingManager = JavaDocHighlightingManagerImpl.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head></head><body>");

        if (path != null) {
            String raw = createElementLink(LINK_PREFIX_NOP_VFS + path, path);

            HtmlChunk html = HtmlChunk.div().setClass("bottom") //
                                      .children( //
                                                 HtmlChunk.tag("icon").attr("src", "AllIcons.Nodes.Package"),
                                                 HtmlChunk.nbsp(),
                                                 HtmlChunk.raw(raw));
            html.appendTo(sb);
        }

        {
            sb.append(DocumentationMarkup.DEFINITION_START);

            appendStyledSpan(sb, highlightingManager.getKeywordAttributes(), mainTitle);
            if (StringHelper.isNotBlank(subTitle)) {
                sb.append(" - ");
                appendStyledSpan(sb, highlightingManager.getClassNameAttributes(), subTitle);
            }

            StringBuilder sb1 = new StringBuilder();
            if (required != null) {
                appendStyledSpan(sb1,
                                 highlightingManager.getInstanceFieldAttributes(),
                                 required
                                 ? NopPluginBundle.message("xlang.doc.flag.required")
                                 : NopPluginBundle.message("xlang.doc.flag.option"));
            }
            if (stdDomain != null) {
                if (!sb1.isEmpty()) {
                    sb1.append(' ');
                }
                appendStyledSpan(sb1, highlightingManager.getLocalVariableAttributes(), stdDomain);
            }
            if (!sb1.isEmpty()) {
                sb.append("\n\n").append(sb1);
            }

            sb.append(DocumentationMarkup.DEFINITION_END);
        }

        if (!StringHelper.isBlank(desc)) {
            String raw = markdown(desc);

            sb.append(DocumentationMarkup.CONTENT_START);
            sb.append(raw);
            sb.append(DocumentationMarkup.CONTENT_END);
        }

        sb.append("</body></html>");

        return !sb.isEmpty() ? sb.toString() : null;
    }

    public static PsiElement createElementForLink(PsiElement context, String link) {
        if (link.startsWith(LINK_PREFIX_NOP_VFS)) {
            String path = link.substring(LINK_PREFIX_NOP_VFS.length());

            return new NopVirtualFile(context, path);
        }
        return null;
    }

    /** 对于多行文本，行首的 <code>&gt; </code> 将被去除后，再按照 markdown 渲染得到 html 代码 */
    private static String markdown(String text) {
        text = text.replaceAll("(?m)^> ", "");
        text = MarkdownHelper.renderHtml(text);

        return text;
    }

    // <<<<<<<<<<<<<<< Source from com.intellij.lang.java.JavaDocumentationProvider
    private static String createElementLink(String path, String text) {
        // 参考 com.intellij.codeInsight.documentation.DocumentationManagerUtil#createHyperlinkImpl
        return "<a href=\""
               + DocumentationManagerProtocol.PSI_ELEMENT_PROTOCOL
               + path
               + "\">"
               + "<code style='font-size:100%;'>"
               + text
               + "</code></a>";
    }

    private static void appendStyledSpan(
            @NotNull StringBuilder sb, @Nullable String value, String @NotNull ... properties
    ) {
        HtmlSyntaxInfoUtil.appendStyledSpan(sb, StringHelper.escapeXml(value), properties);
    }

    private static void appendStyledSpan(
            @NotNull StringBuilder sb, @NotNull TextAttributes attributes, @Nullable String value
    ) {
        HtmlSyntaxInfoUtil.appendStyledSpan(sb,
                                            attributes,
                                            StringHelper.escapeXml(value),
                                            DocumentationSettings.getHighlightingSaturation(false));
    }
    // >>>>>>>>>>>>>>>>>>>>>>>
}

/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.core.lang.json.parse;

import io.nop.api.core.json.JsonParseOptions;
import io.nop.api.core.util.SourceLocation;
import io.nop.commons.io.stream.ICharReader;
import io.nop.commons.text.tokenizer.TextScanner;
import io.nop.core.lang.json.IJsonHandler;
import io.nop.core.lang.json.handler.BuildJObjectJsonHandler;
import io.nop.core.lang.json.handler.BuildObjectJsonHandler;
import io.nop.core.lang.json.yaml.YamlParser;
import io.nop.core.resource.component.parse.AbstractCharReaderResourceParser;

import static io.nop.core.CoreConfigs.CFG_JSON_MAX_NESTED_LEVEL;
import static io.nop.core.CoreErrors.ERR_HANDLER_EXCEED_MAX_NESTED_LEVEL;
import static io.nop.core.CoreErrors.ERR_JSON_DOC_NOT_END_PROPERLY;
import static io.nop.core.CoreErrors.ERR_JSON_STRICT_MODEL_KEY_NOT_DOUBLE_QUOTED;
import static io.nop.core.CoreErrors.ERR_JSON_STRICT_MODEL_STRING_NOT_DOUBLE_QUOTED;
import static io.nop.core.CoreErrors.ERR_JSON_UNEXPECTED_CHAR;

public class JsonParser extends AbstractCharReaderResourceParser<Object> implements IJsonParser {
    public static IJsonParser instance(JsonParseOptions options) {
        if (options != null && options.isYaml())
            return new YamlParser().config(options);
        return new JsonParser().config(options);
    }

    public JsonParser() {
    }

    private int maxDepth = CFG_JSON_MAX_NESTED_LEVEL.get();
    private int _depth;

    // AI返回的XML可能有错误，这里进行一定的容错
    private boolean looseSyntax;
    private boolean strictMode = false;
    private boolean keepComment = false;
    private boolean intern = false;
    private boolean checkEndAfterParse = true;
    private IJsonHandler handler;

    protected void incParseDepth(TextScanner sc) {
        _depth++;
        if (_depth > maxDepth) {
            throw sc.newError(ERR_HANDLER_EXCEED_MAX_NESTED_LEVEL);
        }
    }

    protected void decParseDepth() {
        _depth--;
    }

    public JsonParser config(JsonParseOptions options) {
        if (options != null) {
            this.defaultEncoding(options.getDefaultEncoding()).intern(options.isIntern())
                    .keepComment(options.isKeepComment()).shouldTraceDepends(options.isTraceDepends())
                    .strictMode(options.isStrictMode());
            if (options.isKeepComment() || options.isKeepLocation()) {
                this.handler(new BuildJObjectJsonHandler());
            }
        }
        return this;
    }

    public JsonParser looseSyntax(boolean looseSyntax) {
        this.looseSyntax = looseSyntax;
        return this;
    }

    public JsonParser handler(IJsonHandler writer) {
        this.handler = writer;
        return this;
    }

    public JsonParser intern(boolean intern) {
        this.intern = intern;
        return this;
    }

    public JsonParser shouldTraceDepends(boolean b) {
        return (JsonParser) super.shouldTraceDepends(b);
    }

    public JsonParser defaultEncoding(String encoding) {
        this.setEncoding(encoding);
        return this;
    }

    public JsonParser keepComment(boolean keepComment) {
        this.keepComment = keepComment;
        return this;
    }

    public JsonParser strictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    public JsonParser checkEndAfterParse(boolean checkEndAfterParse) {
        this.checkEndAfterParse = checkEndAfterParse;
        return this;
    }

    protected Object doParse(SourceLocation loc, ICharReader reader) {
        TextScanner sc = TextScanner.fromReader(loc, reader);
        return parseJsonDoc(sc);
    }

    public Object parseJsonDoc(TextScanner sc) {
        if (handler == null)
            handler = new BuildObjectJsonHandler();

        skipBlankAndComment(sc);
        handler.beginDoc(getEncoding());

        switch (sc.cur) {
            case '[':
                array(sc);
                break;
            case '{':
                map(sc);
                break;
            default:
                handler.value(sc.location(), parseLiteral(sc));
        }

        if (checkEndAfterParse && !sc.isEnd())
            throw sc.newError(ERR_JSON_DOC_NOT_END_PROPERLY);
        return handler.endDoc();
    }

    void parseComment(TextScanner sc) {
        String comment = sc.skipJavaComment(keepComment);
        if (comment.length() > 0) {
            handler.comment(comment);
        }
    }

    void object(TextScanner sc) {
        switch (sc.cur) {
            case '[':
                array(sc);
                return;
            case '{':
                map(sc);
                return;
            default:
                SourceLocation loc = sc.location();
                Object value = parseLiteral(sc);
                handler.value(loc, value);
        }
    }

    public Object parseLiteral(TextScanner sc) {
        switch (sc.cur) {
            case '\"':
            case '\'':
                return string(sc);
            case 'n':
                return nullExpr(sc);
            case 't':
                return trueExpr(sc);
            case 'f':
                return falseExpr(sc);
            default:
                if (sc.maybeNumber()) {
                    return number(sc);
                } else {
                    throw sc.newError(ERR_JSON_UNEXPECTED_CHAR);
                }
        }
    }

    Object nullExpr(TextScanner sc) {
        sc.match("null");
        skipComment(sc);
        return null;
    }

    Object trueExpr(TextScanner sc) {
        sc.match("true");
        skipComment(sc);
        return Boolean.TRUE;
    }

    Object falseExpr(TextScanner sc) {
        sc.match("false");
        skipComment(sc);
        return Boolean.FALSE;
    }

    void array(TextScanner sc) {
        SourceLocation loc = sc.location();
        sc.match('[');
        skipComment(sc);
        handler.beginArray(loc);

        if (sc.tryMatch(']')) {
            skipComment(sc);
            handler.endArray();
            return;
        }

        incParseDepth(sc);

        object(sc);

        while (matchComma(sc)) {
            if (!strictMode && sc.cur == ']')
                break;
            object(sc);
        }
        sc.match(']');
        skipComment(sc);

        decParseDepth();

        handler.endArray();
    }

    void map(TextScanner sc) {
        SourceLocation loc = sc.location();
        handler.beginObject(loc);

        sc.match('{');
        skipComment(sc);
        if (sc.tryMatch('}')) {
            skipComment(sc);
            handler.endObject();
            return;
        }

        incParseDepth(sc);

        keyValue(sc);
        while (matchComma(sc)) {
            if (!strictMode && sc.cur == '}')
                break;
            keyValue(sc);
        }
        sc.match('}');
        skipComment(sc);

        decParseDepth();
        handler.endObject();
    }

    void keyValue(TextScanner sc) {
        String key = key(sc);
        handler.key(key);
        if (looseSyntax && sc.cur == ',') {
            handler.value(sc.location(), key);
        } else {
            sc.match(':');
            if (looseSyntax)
                sc.tryMatch(':');
            skipComment(sc);

            object(sc);
        }
    }

    boolean matchComma(TextScanner sc) {
        if (sc.tryConsume(',')) {
            skipBlankAndComment(sc);
            return true;
        }
        return false;
    }

    void skipBlankAndComment(TextScanner sc) {
        sc.skipBlank();
        skipComment(sc);
    }

    void skipComment(TextScanner sc) {
        if (!strictMode) {
            parseComment(sc);
        }
    }

    String key(TextScanner sc) {
        if (strictMode && sc.cur != '\"')
            throw sc.newError(ERR_JSON_STRICT_MODEL_KEY_NOT_DOUBLE_QUOTED);

        if (sc.cur == '\'' || sc.cur == '\"') {
            String str = nextString(sc);
            skipBlankAndComment(sc);
            return tryIntern(str);
        }

        String str = tryIntern(sc.nextJavaVar());
        skipBlankAndComment(sc);
        return str;
    }

    String nextString(TextScanner sc) {
        if (looseSyntax)
            return sc.nextLooseJsonString();
        return sc.nextJsonString();
    }

    String tryIntern(String str) {
        return intern ? str.intern() : str;
    }

    String string(TextScanner sc) {
        if (strictMode && sc.cur != '\"')
            throw sc.newError(ERR_JSON_STRICT_MODEL_STRING_NOT_DOUBLE_QUOTED);
        String str = nextString(sc);
        skipBlankAndComment(sc);
        return str;
    }

    Number number(TextScanner sc) {
        Number n = sc.nextNumber();
        skipBlankAndComment(sc);
        return n;
    }
}
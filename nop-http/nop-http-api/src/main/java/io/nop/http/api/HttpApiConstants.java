/**
 * Copyright (c) 2017-2024 Nop Platform. All rights reserved.
 * Author: canonical_entropy@163.com
 * Blog:   https://www.zhihu.com/people/canonical-entropy
 * Gitee:  https://gitee.com/canonical-entropy/nop-entropy
 * Github: https://github.com/entropy-cloud/nop-entropy
 */
package io.nop.http.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * http header大小写不敏感，全部转成小写
 */
public interface HttpApiConstants {
    String HEADER_RANGE = "range";

    String HEADER_IF_MODIFIED_SINCE = "if-modified-since";

    String HEADER_LAST_MODIFIED = "last-modified";

    String HEADER_CONTENT_RANGE = "content-range";

    String HEADER_CONTENT_LENGTH = "content-length";

    String HEADER_CONTENT_TYPE = "content-type";

    String HEADER_CONTENT_DISPOSITION = "content-disposition";

    String HEADER_USER_AGENT = "user-agent";

    String HEADER_AUTHORIZATION = "authorization";

    String HEADER_API_KEY = "api-key";

    String HEADER_HOST = "host";

    String HEADER_PROXY_AUTHENTICATE = "proxy-authenticate";

    String HEADER_PROXY_AUTHORIZATION = "proxy-authorization";

    String HEADER_SET_COOKIE = "set-cookie";
    String HEADER_SET_COOKIE2 = "set-cookie2";

    String HEADER_REFERRER = "referrer";

    String HEADER_CONNECTION = "connection";
    String HEADER_EXPECT = "expect";
    String HEADER_UPGRADE = "upgrade";

    Set<String> DISALLOWED_HEADERS = new HashSet<>(Arrays.asList(
            HEADER_HOST, HEADER_CONTENT_LENGTH, HEADER_CONNECTION, HEADER_EXPECT, HEADER_UPGRADE));

    String CONTENT_TYPE_OCTET = "application/octet-stream";
    String CONTENT_TYPE_HTML = "text/html";
    String CONTENT_TYPE_JAVASCRIPT = "text/javascript";
    String CONTENT_TYPE_JSON = "application/json";
    String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";

    String CONTENT_TYPE_FORM_MULTIPART = "multipart/form-data";

    String METHOD_GET = "GET";
    String METHOD_POST = "POST";
    String METHOD_OPTIONS = "OPTIONS";
    String METHOD_PUT = "PUT";
    String METHOD_DELETE = "DELETE";
    String METHOD_HEAD = "HEAD";

    List<String> HTTP_METHODS = Arrays.asList(METHOD_GET, METHOD_POST, METHOD_OPTIONS, METHOD_PUT, METHOD_DELETE,
            METHOD_HEAD);

    String PROTOCOL_HTTP = "http";
    String PROTOCOL_HTTPS = "https";

    String PROTOCOL_WS = "ws";
    String PROTOCOL_WSS = "wss";

    String DATA_TYPE_FORM = "form";
    String DATA_TYPE_JSON = "json";

    /**
     * 采用form multipart方式编码提交。一般body为Map，Map中的每个元素可能是String或者IResource
     */
    String DATA_TYPE_MULTIPART = "multipart";

    String BEARER_TOKEN_PREFIX = "Bearer ";
}

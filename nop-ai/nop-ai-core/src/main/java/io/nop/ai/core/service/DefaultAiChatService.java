package io.nop.ai.core.service;

import io.nop.ai.core.AiCoreConstants;
import io.nop.ai.core.api.chat.AiChatOptions;
import io.nop.ai.core.api.chat.IAiChatLogger;
import io.nop.ai.core.api.chat.IAiChatService;
import io.nop.ai.core.api.chat.IAiChatSession;
import io.nop.ai.core.api.messages.AiAssistantMessage;
import io.nop.ai.core.api.messages.AiChatExchange;
import io.nop.ai.core.api.messages.AiChatUsage;
import io.nop.ai.core.api.messages.AiMessage;
import io.nop.ai.core.api.messages.AiMessageAttachment;
import io.nop.ai.core.api.messages.AiToolResponseMessage;
import io.nop.ai.core.api.messages.AiUserMessage;
import io.nop.ai.core.api.messages.MessageStatus;
import io.nop.ai.core.api.messages.Prompt;
import io.nop.ai.core.api.messages.ToolCall;
import io.nop.ai.core.api.tool.ToolSpecification;
import io.nop.ai.core.model.LlmModel;
import io.nop.ai.core.model.LlmModelModel;
import io.nop.ai.core.model.LlmRequestModel;
import io.nop.ai.core.model.LlmResponseModel;
import io.nop.api.core.annotations.ioc.InjectValue;
import io.nop.api.core.config.AppConfig;
import io.nop.api.core.convert.ConvertHelper;
import io.nop.api.core.exceptions.NopException;
import io.nop.api.core.time.CoreMetrics;
import io.nop.api.core.util.Guard;
import io.nop.api.core.util.ICancelToken;
import io.nop.commons.cache.ICache;
import io.nop.commons.cache.MapCache;
import io.nop.commons.concurrent.ratelimit.DefaultRateLimiter;
import io.nop.commons.concurrent.ratelimit.IRateLimiter;
import io.nop.commons.util.FileHelper;
import io.nop.commons.util.StringHelper;
import io.nop.core.lang.eval.IEvalScope;
import io.nop.core.lang.json.JsonTool;
import io.nop.core.reflect.bean.BeanTool;
import io.nop.core.resource.component.ResourceComponentManager;
import io.nop.http.api.client.HttpRequest;
import io.nop.http.api.client.IHttpClient;
import io.nop.xlang.api.XLang;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;

import static io.nop.ai.core.AiCoreConfigs.CFG_AI_SERVICE_DEFAULT_LLM;
import static io.nop.ai.core.AiCoreConfigs.CFG_AI_SERVICE_LOG_MESSAGE;
import static io.nop.ai.core.AiCoreConstants.CONFIG_VAR_LLM_API_KEY;
import static io.nop.ai.core.AiCoreConstants.CONFIG_VAR_LLM_BASE_URL;
import static io.nop.ai.core.AiCoreConstants.PLACE_HOLDER_LLM_NAME;
import static io.nop.ai.core.AiCoreErrors.ARG_CONFIG_VAR;
import static io.nop.ai.core.AiCoreErrors.ARG_HTTP_STATUS;
import static io.nop.ai.core.AiCoreErrors.ARG_LLM_NAME;
import static io.nop.ai.core.AiCoreErrors.ARG_OPTION_NAME;
import static io.nop.ai.core.AiCoreErrors.ARG_PROP_PATH;
import static io.nop.ai.core.AiCoreErrors.ERR_AI_SERVICE_HTTP_ERROR;
import static io.nop.ai.core.AiCoreErrors.ERR_AI_SERVICE_NO_BASE_URL;
import static io.nop.ai.core.AiCoreErrors.ERR_AI_SERVICE_NO_DEFAULT_LLMS;
import static io.nop.ai.core.AiCoreErrors.ERR_AI_SERVICE_OPTION_NOT_SET;

public class DefaultAiChatService implements IAiChatService {
    static final Logger LOG = LoggerFactory.getLogger(DefaultAiChatService.class);

    private IHttpClient httpClient;

    private IAiChatLogger chatLogger;

    private Map<String, IRateLimiter> rateLimiters = new ConcurrentHashMap<>();

    protected MockAiChatService mockService = new MockAiChatService();

    private ICache<String, String> secretCache = new MapCache<>("ai-cache", true);

    private File secretDir;

    @InjectValue("@cfg:nop.ai.secret-dir|/nop/ai/secret")
    public void setSecretDir(File secretDir) {
        this.secretDir = secretDir;
    }

    @Inject
    public void setChatLogger(IAiChatLogger chatLogger) {
        this.chatLogger = chatLogger;
    }

    @Inject
    public void setHttpClient(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected synchronized IRateLimiter getRateLimiter(String llmName) {
        IRateLimiter rateLimiter = rateLimiters.get(llmName);
        if (rateLimiter != null)
            return rateLimiter;

        LlmModel llmModel = loadLlmModel(llmName);
        Double rateLimit = llmModel.getRateLimit();
        if (rateLimit == null)
            return null;

        rateLimiter = new DefaultRateLimiter(rateLimit);
        rateLimiters.put(llmName, rateLimiter);
        return rateLimiter;
    }

    protected String getLlmName(AiChatOptions options) {
        String llm = options.getProvider();
        if (llm == null)
            llm = CFG_AI_SERVICE_DEFAULT_LLM.get();
        if (StringHelper.isEmpty(llm))
            throw new NopException(ERR_AI_SERVICE_NO_DEFAULT_LLMS);
        return llm;
    }

    protected LlmModel loadLlmModel(String llmName) {
        String modelPath = buildLlmModelPath(llmName);
        return (LlmModel) ResourceComponentManager.instance().loadComponentModel(modelPath);
    }

    protected String buildLlmModelPath(String llmName) {
        Guard.checkArgument(!llmName.contains(".."), "llmName");
        return "/nop/ai/llm/" + llmName + ".llm.xml";
    }

    @Override
    public IAiChatSession newSession(AiChatOptions options) {
        DefaultAiChatSession session = new DefaultAiChatSession();
        session.setSessionId(generateSessionId());
        return session;
    }

    protected String generateSessionId() {
        return StringHelper.generateUUID();
    }

    @Override
    public CompletionStage<AiChatExchange> sendChatAsync(Prompt prompt, AiChatOptions options, ICancelToken cancelToken) {
        Guard.notNull(options, "chatOptions");

        if (AiCoreConstants.MODEL_MOCK.equals(options.getModel())) {
            return mockService.sendChatAsync(prompt, options, cancelToken);
        }

        String llmName = getLlmName(options);
        LlmModel llmModel = loadLlmModel(llmName);

        IRateLimiter rateLimiter = getRateLimiter(llmName);

        if (rateLimiter != null)
            rateLimiter.acquire();
        return doSendChat(llmName, llmModel, prompt, options, cancelToken);
    }

    protected CompletionStage<AiChatExchange> doSendChat(String llmName,
                                                         LlmModel llmModel,
                                                         Prompt prompt, AiChatOptions options,
                                                         ICancelToken cancelToken) {
        long beginTime = CoreMetrics.currentTimeMillis();
        AiChatExchange chatExchange = new AiChatExchange();
        chatExchange.setPrompt(prompt);
        chatExchange.setChatOptions(options);
        chatExchange.setBeginTime(CoreMetrics.currentTimeMillis());
        chatExchange.setExchangeId(StringHelper.generateUUID());

        boolean logMessage = CFG_AI_SERVICE_LOG_MESSAGE.get();
        if (logMessage) {
            chatLogger.logRequest(chatExchange);
        }

        HttpRequest request = buildHttpRequest(llmName, llmModel, prompt, options);

        IEvalScope scope = XLang.newEvalScope();

        if (llmModel.getBuildHttpRequest() != null) {
            llmModel.getBuildHttpRequest().call3(null, request, prompt, options, scope);
        }

        return httpClient.fetchAsync(request, cancelToken).thenApply(
                res -> {
                    if (res.getHttpStatus() != 200)
                        throw new NopException(ERR_AI_SERVICE_HTTP_ERROR)
                                .param(ARG_LLM_NAME, llmName).param(ARG_HTTP_STATUS, res.getHttpStatus());

                    Map<String, Object> response = res.getBodyAsBean(Map.class);

                    parseHttpResponse(llmName, llmModel, response, chatExchange);

                    long endTime = CoreMetrics.currentTimeMillis();
                    chatExchange.setUsedTime((int) (endTime - beginTime));

                    if (llmModel.getParseHttpResponse() != null) {
                        llmModel.getParseHttpResponse().call3(null, response, chatExchange, options, scope);
                    }
                    return chatExchange;
                });
    }

    protected HttpRequest buildHttpRequest(String llmName, LlmModel llmModel,
                                           Prompt prompt, AiChatOptions options) {
        String model = getModel(llmName, llmModel, options);
        String url = getBaseUrl(llmName, llmModel, model);

        url = StringHelper.appendPath(url, llmModel.getChatUrl());

        String apiKey = getApiKey(llmName);

        HttpRequest request = HttpRequest.post(url);
        if (!StringHelper.isEmpty(apiKey)) {
            if (llmModel.getApiKeyHeader() != null) {
                request.header(llmModel.getApiKeyHeader(), apiKey);
            } else {
                request.setBearerToken(apiKey);
            }
        }

        initHeaders(request, options);
        Map<String, Object> body = new HashMap<>();
        initBody(llmName, llmModel, model, body, prompt, options);
        request.setBody(body);
        return request;
    }

    protected String getBaseUrl(String llmName, LlmModel llmModel, String model) {
        String baseUrlKey = StringHelper.replace(CONFIG_VAR_LLM_BASE_URL, PLACE_HOLDER_LLM_NAME, llmName);
        String baseUrl = (String) AppConfig.var(baseUrlKey);
        if (StringHelper.isEmpty(baseUrl))
            baseUrl = llmModel.getBaseUrl();
        if (StringHelper.isEmpty(baseUrl))
            throw new NopException(ERR_AI_SERVICE_NO_BASE_URL).param(ARG_LLM_NAME, llmName)
                    .param(ARG_CONFIG_VAR, baseUrlKey);
        return StringHelper.renderTemplate(baseUrl, varName -> {
            if ("model".equals(varName))
                return model;
            if ("api-version".equals(varName)) {
                String apiVersion = getApiVersion(llmName);
                if (!StringHelper.isEmpty(apiVersion))
                    return apiVersion;
            }

            String value = AppConfig.var(varName, "");
            if (!StringHelper.isEmpty(value))
                return value;
            return "{" + varName + "}";
        });
    }

    private String getApiVersion(String llmName) {
        String apiVersionKey = StringHelper.replace(CONFIG_VAR_LLM_API_KEY, PLACE_HOLDER_LLM_NAME, llmName);
        return AppConfig.var(apiVersionKey, "");
    }

    protected String getApiKey(String llmName) {
        String apiKeyName = StringHelper.replace(CONFIG_VAR_LLM_API_KEY, PLACE_HOLDER_LLM_NAME, llmName);
        String apiKey = (String) AppConfig.var(apiKeyName);
        if (StringHelper.isEmpty(apiKey)) {
            return secretCache.computeIfAbsent(llmName, k -> {
                File secretFile = new File(secretDir, llmName + ".txt");
                if (secretFile.exists()) {
                    String secret = StringHelper.strip(FileHelper.readText(secretFile, null));
                    if (secret != null) {
                        AppConfig.getConfigProvider().assignConfigValue(apiKeyName, secret);
                        return secret;
                    }
                }
                return "";
            });
        }
        return apiKey;
    }

    protected void initHeaders(HttpRequest request, AiChatOptions options) {
        Long requestTimeout = options.getRequestTimeout();
        if (requestTimeout != null) {
            request.timeout(requestTimeout);
        }
    }

    /**
     * {
     * "messages": [
     * {
     * "content": "You are a helpful assistant",
     * "role": "system"
     * },
     * {
     * "content": "Hi",
     * "role": "user"
     * }
     * ],
     * "model": "deepseek-chat",
     * "frequency_penalty": 0,
     * "max_tokens": 2048,
     * "presence_penalty": 0,
     * "response_format": {
     * "type": "text"
     * },
     * "stop": null,
     * "stream": false,
     * "stream_options": null,
     * "temperature": 1,
     * "top_p": 1,
     * "tools": null,
     * "tool_choice": "none",
     * "logprobs": false,
     * "top_logprobs": null
     * }
     */
    protected void initBody(String llmName,
                            LlmModel llmModel, String model, Map<String, Object> body,
                            Prompt prompt, AiChatOptions options) {
        body.put("model", model);
        body.put("stream", false);

        body.put("response_format", Map.of("type", "text"));
        List<Map<String, Object>> messages = new ArrayList<>();
        body.put("messages", messages);

        setOptions(llmModel, body, prompt, options);

        LlmModelModel modelModel = getModelModel(llmModel, model);
        List<AiMessage> msgs = prompt.getMessages();
        AiMessage lastMessage = prompt.getLastMessage();
        for (AiMessage msg : msgs) {
            Map<String, Object> msgMap = toMessage(msg, modelModel, lastMessage == msg, options);
            messages.add(msgMap);
        }

        addTools(body, prompt);
    }

    protected Map<String, Object> toMessage(AiMessage msg, LlmModelModel modelModel, boolean last, AiChatOptions options) {
        Object content = getMessageContent(msg);
        if (modelModel != null && last) {
            if (Boolean.TRUE.equals(options.getEnableThinking())) {
                if (modelModel.getEnableThinkingPrompt() != null) {
                    content += "\n" + modelModel.getEnableThinkingPrompt();
                }
            } else {
                if (modelModel.getDisableThinkingPrompt() != null) {
                    content += "\n" + modelModel.getDisableThinkingPrompt();
                }
            }
        }

        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("role", getRole(msg));
        ret.put("content", content);

        if (msg instanceof AiAssistantMessage) {
            List<ToolCall> toolCalls = ((AiAssistantMessage) msg).getToolCalls();
            if (toolCalls != null && !toolCalls.isEmpty()) {
                addToolCalls(ret, toolCalls);
            }
        } else if (msg instanceof AiToolResponseMessage) {
            AiToolResponseMessage resMsg = (AiToolResponseMessage) msg;
            ret.put("tool_call_id", resMsg.getToolCallId());
            ret.put("name", resMsg.getName());
        }
        return ret;
    }

    protected void addToolCalls(Map<String, Object> message, List<ToolCall> toolCalls) {
        List<Map<String, Object>> json = new ArrayList<>();
        for (ToolCall toolCall : toolCalls) {
            Map<String, Object> call = new LinkedHashMap<>();
            Map<String, Object> fnJson = new LinkedHashMap<>();
            fnJson.put("name", toolCall.getName());
            fnJson.put("arguments", JsonTool.stringify(toolCall.getArguments()));
            call.put("function", fnJson);

            call.put("id", toolCall.getId());
            call.put("type", "function");
            json.add(call);
        }
        message.put("tool_calls", json);
    }

    protected void addTools(Map<String, Object> body, Prompt prompt) {
        if (prompt.getTools() == null || prompt.getTools().isEmpty())
            return;

        List<Map<String, Object>> toolsJson = new ArrayList<>();
        for (ToolSpecification spec : prompt.getTools()) {
            Map<String, Object> toolJson = new LinkedHashMap<>();
            toolJson.put("type", "function");
            Map<String, Object> funcJson = new LinkedHashMap<>();
            toolJson.put("function", funcJson);

            funcJson.put("name", spec.getName());
            funcJson.put("description", spec.getDescription());
            funcJson.put("parameters", spec.getInputSchema());

            toolsJson.add(toolJson);
        }

        body.put("tools", toolsJson);
    }

    protected Object getMessageContent(AiMessage message) {
        if (message instanceof AiUserMessage) {
            List<AiMessageAttachment> resources = ((AiUserMessage) message).getAttachments();
            if (resources != null && !resources.isEmpty()) {
                List<Map<String, Object>> list = new ArrayList<>();
                if (message.getContent() != null)
                    list.add(Map.of("type", "text", "text", message.getContent()));
                for (AiMessageAttachment resource : resources) {
                    list.add(resource.toContent());
                }
                return list;
            }
        }
        return message.getContent();
    }

    protected LlmModelModel getModelModel(LlmModel llmModel, String modelName) {
        LlmModelModel model = llmModel.getModel(modelName);
        if (model == null) {
            String baseModel = StringHelper.firstPart(modelName, ':');
            if (!baseModel.equals(modelName)) {
                model = llmModel.getModel(baseModel);
            }
        }
        return model;
    }

    protected String getModel(String llmName, LlmModel llmModel, AiChatOptions options) {
        String model = options.getModel();
        if (StringHelper.isEmpty(model))
            model = llmModel.getDefaultModel();
        if (StringHelper.isEmpty(model))
            throw new NopException(ERR_AI_SERVICE_OPTION_NOT_SET)
                    .param(ARG_LLM_NAME, llmName).param(ARG_OPTION_NAME, "model");
        if (llmModel.getAliasMap() != null) {
            String realName = llmModel.getAliasMap().get(model);
            if (realName != null)
                model = realName;
        }
        return model;
    }

    protected void setOptions(LlmModel llmModel, Map<String, Object> body, Prompt prompt, AiChatOptions options) {
        LlmRequestModel requestModel = llmModel.getRequest();
        if (requestModel == null)
            return;

        LlmModelModel model = getModelModel(llmModel, options.getModel());

        setIfNotNull(body, requestModel.getSeedPath(), options.getSeed());
        Integer maxTokens = options.getMaxTokens();
        if (maxTokens == null) {
            if (model != null)
                maxTokens = model.getDefaultMaxTokens();
        }

        if (maxTokens != null) {
            if (model != null && model.getMaxTokensLimit() != null && model.getMaxTokensLimit() < maxTokens)
                maxTokens = model.getMaxTokensLimit();
            setIfNotNull(body, requestModel.getMaxTokensPath(), maxTokens);
        }

        setIfNotNull(body, requestModel.getTemperaturePath(), options.getTemperature());

        setIfNotNull(body, requestModel.getTopPPath(), options.getTopP());
        setIfNotNull(body, requestModel.getTopKPath(), options.getTopK());
        setIfNotNull(body, requestModel.getStopPath(), options.getStop());
        setIfNotNull(body, requestModel.getContextLengthPath(), options.getContextLength());
    }

    protected void setIfNotNull(Map<String, Object> body, String propPath, Object value) {
        if (propPath == null)
            return;

        if (value != null)
            BeanTool.setComplexProperty(body, propPath, value);
    }

    protected String getRole(AiMessage message) {
        return message.getRole();
    }

    protected void parseHttpResponse(String llmName, LlmModel llmModel,
                                     Map<String, Object> response, AiChatExchange chatResponse) {

        try {
            parseToResult(chatResponse, llmModel, response);
            checkThink(chatResponse, llmModel, chatResponse.getChatOptions().getModel(), response);


            if (CFG_AI_SERVICE_LOG_MESSAGE.get()) {
                chatLogger.logResponse(chatResponse);
            }
        } catch (Exception e) {
            LOG.info("nop.ai.parse-result-fail", e);
            throw NopException.adapt(e);
        }
    }

    protected void parseToResult(AiChatExchange chatExchange, LlmModel llmModel,
                                 Map<String, Object> result) {
        LlmResponseModel responseModel = llmModel.getResponse();

        String content = getString(result, responseModel.getContentPath());
        chatExchange.setContent(content);

        AiChatUsage usage = new AiChatUsage();

        usage.setPromptTokens(getInteger(result, responseModel.getPromptTokensPath()));
        usage.setTotalTokens(getInteger(result, responseModel.getTotalTokensPath()));
        usage.setCompletionTokens(getInteger(result, responseModel.getCompletionTokensPath()));
        chatExchange.setStatus(getMessageStatus(result, responseModel.getStatusPath()));
        chatExchange.setUsage(usage);

        List<ToolCall> toolCalls = parseToolCalls(llmModel, result);
        chatExchange.makeResponse().setToolCalls(toolCalls);
    }

    protected List<ToolCall> parseToolCalls(LlmModel llmModel, Map<String, Object> result) {
        String toolCallsPath = llmModel.getResponse().getToolCallsPath();
        if (StringHelper.isEmpty(toolCallsPath))
            return null;

        List<Map<String, Object>> toolCalls = (List<Map<String, Object>>) BeanTool.getComplexProperty(result, toolCallsPath);
        if (toolCalls == null || toolCalls.isEmpty())
            return null;

        List<ToolCall> ret = new ArrayList<>();
        for (Map<String, Object> map : toolCalls) {
            int index = ConvertHelper.toPrimitiveInt(map.get("index"), 0, NopException::new);
            String callId = map.get("id").toString();

            String name = (String) BeanTool.getComplexProperty(map, "function.name");
            Object arguments = BeanTool.getComplexProperty(map, "function.arguments");
            Map<String, Object> argsMap = null;
            if (arguments instanceof Map) {
                argsMap = (Map<String, Object>) arguments;
            } else if (arguments instanceof String) {
                argsMap = JsonTool.parseMap(arguments.toString());
            }

            ToolCall tc = new ToolCall();
            tc.setIndex(index);
            tc.setName(name);
            tc.setArguments(argsMap);
            tc.setId(callId);
            ret.add(tc);
        }
        return ret;
    }

    protected MessageStatus getMessageStatus(Map<String, Object> body, String propPath) {
        String status = getString(body, propPath);
        if (status == null)
            return null;
        return MessageStatus.END;
    }

    protected String getString(Map<String, Object> result, String path) {
        if (path == null)
            return null;
        return StringHelper.toString(BeanTool.getComplexProperty(result, path), null);
    }

    protected Integer getInteger(Map<String, Object> result, String path) {
        if (path == null)
            return null;
        return ConvertHelper.toInteger(BeanTool.getComplexProperty(result, path),
                err -> new NopException(err).param(ARG_PROP_PATH, path));
    }

    protected void checkThink(AiChatExchange chatResponse, LlmModel model, String modelName, Map<String, Object> result) {
        if (chatResponse.getThink() != null)
            return;

        if (model.getResponse().getReasoningContentPath() != null) {
            String thinking = (String) BeanTool.getComplexProperty(result, model.getResponse().getReasoningContentPath());
            if (!StringHelper.isEmpty(thinking)) {
                chatResponse.setThink(thinking);
                return;
            }
        }

        LlmModelModel modelModel = getModelModel(model, modelName);
        String startMarker = modelModel == null || modelModel.getThinkStartMarker() == null ? "<think>\n" : modelModel.getThinkStartMarker();
        String endMarker = modelModel == null || modelModel.getThinkEndMarker() == null ? "\n</think>\n" : modelModel.getThinkEndMarker();

        String content = chatResponse.getContent();
        if (content != null) {
            boolean bThink = content.startsWith(startMarker);
            if (bThink) {
                int pos2 = content.indexOf(endMarker);
                if (pos2 > 0) {
                    String think = content.substring(startMarker.length(), pos2);
                    chatResponse.setThink(think);
                    pos2 += endMarker.length();
                    if (pos2 < content.length() && content.charAt(pos2) == '\n')
                        pos2++;

                    chatResponse.setContent(content.substring(pos2));
                }
            }
        }
    }

    @Override
    public IAiChatSession getSession(String sessionId) {
        return null;
    }
}
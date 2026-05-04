package com.utils;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.List;

public class AIUitl {

    private static final String API_KEY = "34eb5f5a-bee1-488f-a9b2-e31d7420fd77";
    private static final String MODEL = "ep-20250126163455-8xrmx";
    private static final String SYSTEM_PROMPT = "你是一位专业的学习辅导专家，擅长解答各类学习问题。请用清晰、准确的语言回答问题，必要时可以分点说明。";

    // 单例 ArkService，避免每次请求都新建/销毁
    private static final ArkService SERVICE = new ArkService(API_KEY);

    /**
     * 带历史上下文的多轮对话（推荐使用）
     *
     * @param history 历史对话列表，每个元素包含 ask（用户问题）和 reply（AI回答）
     * @param question 当前用户问题
     * @return AI 回答文本
     */
    public static String getResponseWithHistory(List<HistoryMessage> history, String question) {
        List<ChatMessage> messages = buildMessages(history, question);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(messages)
                .build();

        StringBuilder result = new StringBuilder();
        SERVICE.createChatCompletion(request)
                .getChoices()
                .forEach(choice -> result.append(choice.getMessage().getContent()));

        return result.toString();
    }

    /**
     * 带历史上下文的流式对话，通过回调逐块返回内容
     *
     * @param history  历史对话列表
     * @param question 当前用户问题
     * @param callback 每收到一个文本块时的回调，参数为文本片段；返回 false 则中断流
     */
    public static void streamResponseWithHistory(List<HistoryMessage> history, String question,
                                                  StreamCallback callback) {
        List<ChatMessage> messages = buildMessages(history, question);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(messages)
                .build();

        SERVICE.streamChatCompletion(request).blockingForEach(chunk -> {
            if (chunk.getChoices() != null && !chunk.getChoices().isEmpty()) {
                Object content = chunk.getChoices().get(0).getMessage().getContent();
                if (content != null) {
                    String text = content.toString();
                    if (!text.isEmpty()) {
                        callback.onChunk(text);
                    }
                }
            }
        });
    }

    /**
     * 构建发送给 AI 的消息列表（system + 历史 + 当前问题）
     * 最多携带最近 5 轮历史，避免 token 过多
     */
    private static List<ChatMessage> buildMessages(List<HistoryMessage> history, String question) {
        List<ChatMessage> messages = new ArrayList<>();

        // system 角色
        messages.add(ChatMessage.builder()
                .role(ChatMessageRole.SYSTEM)
                .content(SYSTEM_PROMPT)
                .build());

        // 最近 5 轮历史（每轮 = 1 条用户 + 1 条 AI）
        if (history != null && !history.isEmpty()) {
            int start = Math.max(0, history.size() - 5);
            for (int i = start; i < history.size(); i++) {
                HistoryMessage h = history.get(i);
                if (h.getAsk() != null && !h.getAsk().isEmpty()) {
                    messages.add(ChatMessage.builder()
                            .role(ChatMessageRole.USER)
                            .content(h.getAsk())
                            .build());
                }
                if (h.getReply() != null && !h.getReply().isEmpty()) {
                    messages.add(ChatMessage.builder()
                            .role(ChatMessageRole.ASSISTANT)
                            .content(h.getReply())
                            .build());
                }
            }
        }

        // 当前问题
        messages.add(ChatMessage.builder()
                .role(ChatMessageRole.USER)
                .content(question)
                .build());

        return messages;
    }

    /**
     * 流式回调接口
     */
    public interface StreamCallback {
        void onChunk(String text);
    }

    /**
     * 历史消息数据类
     */
    public static class HistoryMessage {
        private String ask;
        private String reply;

        public HistoryMessage(String ask, String reply) {
            this.ask = ask;
            this.reply = reply;
        }

        public String getAsk() { return ask; }
        public String getReply() { return reply; }
    }
}

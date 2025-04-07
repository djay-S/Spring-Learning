package com.example.ai.chat.controller;

import com.example.ai.model.PromptBody;
import com.example.ai.model.record.GeminiModel;
import com.example.ai.model.record.ModelListResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ChatController {

    @Value("${spring.ai.openai.api-key}")
    private String GEMINI_API_KEY;
    private final RestClient restClient;
    private final ChatClient chatClient;

    public ChatController(RestClient.Builder builder, ChatClient.Builder chatBuilder) {
        this.restClient = builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
        this.chatClient = chatBuilder.build();
    }

    @GetMapping("/models")
    public List<GeminiModel> models() {
        ResponseEntity<ModelListResponse> response = restClient.get()
                .uri("/v1beta/openai/models")
                .header("Authorization","Bearer " + GEMINI_API_KEY)
                .retrieve()
                .toEntity(ModelListResponse.class);
        return Objects.requireNonNull(response.getBody()).data();
    }

    @GetMapping("/prompt")
    public Map<String, String> getPromptResponse(@RequestBody PromptBody prompt) {
        Map<String, String> responseMap = new LinkedHashMap<>();
        responseMap.put("prompt", prompt.getPrompt());
        String response = chatClient.prompt(prompt.getPrompt())
                .call()
                .content();
        responseMap.put("response", response);
        return responseMap;
    }
}

package com.example.ai.model.record;

import java.util.List;

public record ModelListResponse(String object, List<GeminiModel> data) {}

package com.bam.lemmecook.service;

import com.bam.lemmecook.dto.response.ResponseGeminiAnswerDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${lemme-cook-gemini-key}")
    private String API_KEY;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public GeminiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseGeminiAnswerDTO getGeminiResponse(String prompt) {
        // create the Gemini API header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", prompt),
                }),
        });

        String jsonBody = null;
        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // send a POST request to Gemini API
        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_API_URL + API_KEY, HttpMethod.POST, entity, String.class);

        // return response body
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode candidatesNode = rootNode.path("candidates");
            if (candidatesNode.isArray() && candidatesNode.size() > 0) {
                JsonNode candidate = candidatesNode.get(0);
                JsonNode contentNode = candidate.path("content");
                JsonNode partsNode = contentNode.path("parts");
                if (partsNode.isArray() && partsNode.size() > 0) {
                    JsonNode partNode = partsNode.get(0);
                    return new ResponseGeminiAnswerDTO(partNode.path("text").asText());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}

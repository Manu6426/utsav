package com.utsav.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Plug-in point for the Utsav AI recommendation bot.
 *
 * <p>The web frontend currently runs its own heuristic recommendation engine
 * (token-overlap scoring + review-weighted ranking) in JavaScript so the bot
 * works with zero backend dependencies. When we are ready for true natural
 * language understanding, this service becomes the server-side brain:
 *
 * <p>TODO (Phase 3): wire an LLM provider here.
 * <ol>
 *   <li>Add {@code spring-ai-openai-spring-boot-starter} (or the Anthropic
 *       equivalent) to {@code pom.xml}.</li>
 *   <li>Bind the key via environment: {@code LLM_API_KEY} -&gt;
 *       {@code spring.ai.openai.api-key=${LLM_API_KEY}} in
 *       {@code application-prod.properties}. Never commit a key.</li>
 *   <li>Implement {@link #recommend(String, Map)} to send the user message +
 *       the serialized vendor catalog (id, name, category, city, services,
 *       rating, reviewCount) with a function-calling / JSON-mode prompt that
 *       returns ranked vendor ids plus a conversational reply.</li>
 *   <li>Keep the current JS heuristic as the offline fallback when the LLM
 *       call fails or no key is configured.</li>
 * </ol>
 */
@Service
public class AiRecommendationService {

    /**
     * Rank vendors for a free-text user message.
     *
     * @param message free-text request from the chat bot, e.g.
     *                "candid photographer in Dallas under $2000"
     * @param context conversation context (previous intent, filters, already
     *                shown vendor ids) so follow-ups like "cheaper options"
     *                resolve correctly
     * @return ranked vendor ids with scores and "why" reasons; empty until
     *         the LLM provider is wired (see TODO above)
     */
    public List<Map<String, Object>> recommend(String message, Map<String, Object> context) {
        // Heuristic engine lives in the frontend for now; this is the seam
        // where the LLM takes over in Phase 3.
        return List.of();
    }

    /**
     * Answer customer-care questions (booking status, policies) from in-app data.
     * Same TODO as above: LLM + retrieval over bookings/policies in Phase 3.
     */
    public String answerCareQuestion(String question) {
        return "";
    }
}

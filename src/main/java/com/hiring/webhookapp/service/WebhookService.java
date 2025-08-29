package com.hiring.webhookapp.service;

import com.hiring.webhookapp.model.SolutionRequest;
import com.hiring.webhookapp.model.WebhookRequest;
import com.hiring.webhookapp.model.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;
    private final SqlSolutionService sqlSolutionService;

    public WebhookService(RestTemplate restTemplate, WebClient.Builder webClientBuilder,
            SqlSolutionService sqlSolutionService) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
        this.sqlSolutionService = sqlSolutionService;
    }

    public void processWebhook() {
        try {
            logger.info("Starting webhook process");

            // Step 1: Generate webhook
            WebhookRequest request = new WebhookRequest(
                    "John Doe",
                    "REG12347",
                    "john@example.com");

            logger.info("Sending webhook generation request");
            WebhookResponse response = restTemplate.postForObject(
                    GENERATE_WEBHOOK_URL,
                    request,
                    WebhookResponse.class);

            if (response == null) {
                logger.error("Failed to generate webhook: Null response received");
                return;
            }

            logger.info("Webhook generated successfully: {}", response.getWebhook());
            logger.info("Access token received: {}", response.getAccessToken());

            // Step 2: Determine the question based on the regNo
            String regNo = request.getRegNo();
            boolean isOdd = determineIfOdd(regNo);

            logger.info("RegNo {} is odd: {}", regNo, isOdd);

            // Step 3: Solve the SQL problem
            String sqlQuery = sqlSolutionService.getSqlSolution();
            logger.info("SQL solution: {}", sqlQuery);

            // Step 4: Submit the solution
            SolutionRequest solutionRequest = new SolutionRequest(sqlQuery);

            webClientBuilder.build()
                    .post()
                    .uri(response.getWebhook())
                    .header(HttpHeaders.AUTHORIZATION, response.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(solutionRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(result -> logger.info("Solution submitted successfully: {}", result))
                    .doOnError(error -> logger.error("Error submitting solution: {}", error.getMessage()))
                    .block();

            logger.info("Webhook process completed");

        } catch (Exception e) {
            logger.error("Error in webhook process: {}", e.getMessage(), e);
        }
    }

    private boolean determineIfOdd(String regNo) {
        try {
            // Extract the last two digits
            if (regNo != null && regNo.length() >= 2) {
                String lastTwoDigits = regNo.substring(regNo.length() - 2);
                // Try to parse as integer, if not possible default to even
                try {
                    int number = Integer.parseInt(lastTwoDigits);
                    return number % 2 != 0;
                } catch (NumberFormatException e) {
                    // If last two chars aren't numbers, try just the last char
                    try {
                        int number = Integer.parseInt(regNo.substring(regNo.length() - 1));
                        return number % 2 != 0;
                    } catch (NumberFormatException ex) {
                        // Default to even if still not a number
                        return false;
                    }
                }
            }
            return false; // Default to even if regNo is too short
        } catch (Exception e) {
            logger.error("Error determining if regNo is odd: {}", e.getMessage());
            return false; // Default to even on error
        }
    }
}
package com.coltran.ai.springaidesktop.infrastructure.desktop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestClient;

public class ModelManagerService {
    
    private final RestClient restClient;

    public ModelManagerService(RestClient.Builder restClientBuilder, @Value("${ai.engine.port}") String enginePort) {
        this.restClient = restClientBuilder.baseUrl("http://127.0.0.1:" + enginePort).build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ensureModelIsDownloaded() {
        System.out.println("Checking if llama3.2 is installed...");

        try {

            String tagsResponse = restClient.get()
                .uri("/api/tags")
                .retrieve()
                .body(String.class);

            if(tagsResponse != null && !tagsResponse.contains("llama3.2")) {
                System.out.println("Model not found. Downloading llama3.2 (This may take a while depending on internet speed)... ");

                restClient.post()
                .uri("/api/pull")
                .body(Map.of("name", "llama3.2", "stream", false))
                .retrieve()
                .toBodilessEntity();

                System.out.println("Download complete! AI is ready.");
            } else {
                System.out.println("Model llama3.2 is already installed and ready.");
            }

        } catch (Exception e ) {
            System.err.println("Error communicating with local AI Engine: " + e.getMessage());
        }
    }
}

package com.coltran.ai.springaidesktop.infrastructure.desktop;

import java.io.IOException;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class OllamaOrchestrator implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private Process ollamaProcess; 

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String enginePort = event.getEnvironment().getProperty("OLLAMA_PORT", "11435");
        startOllamaDaemon(enginePort);
    }

    private void startOllamaDaemon(String enginePort) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.environment().put("OLLAMA_HOST", "0.0.0.0:" + enginePort);
            builder.environment().put("OLLAMA_MODELS", System.getProperty("user.home") + "/.coltranai/models");
    
            String os = System.getProperty("os.name").toLowerCase();
    
            if(os.contains("win")) {
                builder.command("cmd.exe", "/c", "ai-engine\\ollama.exe", "serve");
            } else {
                builder.command("ollama", "serve");
            }
    
            ollamaProcess = builder.start();

            System.out.println("Ollama AI Engine started successfully on port " + enginePort);

            Runtime.getRuntime().addShutdownHook(new Thread(this::stopOllamaDaemon));

        } catch (IOException e) {
            System.err.println("Failed to start Ollama AI Engine: " + e.getMessage());
        }
    }

    public void stopOllamaDaemon() {
        if(ollamaProcess != null && ollamaProcess.isAlive()) {
            ollamaProcess.destroy();
            System.out.println("Ollama AI Engine shut down successfully.");
        }
    }
    
}

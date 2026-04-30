package com.coltran.ai.springaidesktop.infrastructure.desktop;

import java.io.File;
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
    
            String executablePath = resolveOllamaExecutable();
            builder.command(executablePath, "serve");


            builder.redirectErrorStream(true);
            builder.redirectOutput(new File(System.getProperty("user.home") + "/.coltranai/ollama-engine.log"));
            
            ollamaProcess = builder.start();

            System.out.println("Ollama AI Engine started successfully on port " + enginePort);

            Runtime.getRuntime().addShutdownHook(new Thread(this::stopOllamaDaemon));

        } catch (IOException e) {
            System.err.println("Failed to start Ollama AI Engine: " + e.getMessage());
        }
    }

    private String resolveOllamaExecutable() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");
        
        String binaryName = isWindows ? "ollama.exe" : "ollama";
        
        String userDir = System.getProperty("user.dir");
        File bundledEngine = new File(userDir, "app/ai-engine/" + binaryName);
        
        if (bundledEngine.exists()) {
            return bundledEngine.getAbsolutePath();
        }
        
        System.out.println("Bundled engine not found at " + bundledEngine.getAbsolutePath() + ". Falling back to system PATH.");
        return binaryName; 
    }

    public void stopOllamaDaemon() {
        if(ollamaProcess != null && ollamaProcess.isAlive()) {
            ollamaProcess.destroy();
            System.out.println("Ollama AI Engine shut down successfully.");
        }
    }
    
}

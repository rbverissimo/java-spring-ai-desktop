package com.coltran.ai.springaidesktop.infrastructure.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

public class OllamaOrchestrator implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private Process ollamaProcess; 
    private static final String OLLAMA_VERSION = "v0.22.0"; //TODO: move into application.yaml

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        String enginePort = event.getEnvironment().getProperty("OLLAMA_PORT", "11435");
        ensureEngineExistsAndStart(enginePort);
    }

    private void ensureEngineExistsAndStart(String enginePort) {
        String userHome = System.getProperty("user.home");

        File coltranBinDir = new File(userHome, ".coltranai/bin");

        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");
        String binaryFileName = isWindows ? "ollama.exe" : "ollama";

        File engineFile = new File(coltranBinDir, binaryFileName);

        if(!engineFile.exists()) {

            if(!coltranBinDir.exists()) coltranBinDir.mkdirs();

            try {

                if(isWindows){
                    downloadAndExtractWindowsZip(engineFile);
                } else {
                    System.out.println("Non-windows OS detected. Assuming Ollama is installed on system PATH.");
                    engineFile = new File("ollama");
                }

            } catch(Exception e) {
                System.err.println("Failed to download or extract the AI Engine: " + e.getMessage());
                return; 
            }

        } else {
            System.out.println("Found existing Ollama engine at" + engineFile.getAbsolutePath());
        }

        startOllamaDaemon(engineFile.getAbsolutePath(), enginePort);
        
    }

    private void downloadAndExtractWindowsZip(File targetExe) throws Exception {
        String url = "https://github.com/ollama/ollama/releases/download/" + OLLAMA_VERSION + "/ollama-windows-amd64.zip";
        File tempZip = new File(targetExe.getParentFile(), "ollama-temp.zip");

        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        Files.copy(response.body(), tempZip.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Download complete. Extracting...");

        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZip))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if(entry.getName().endsWith("ollama.exe")) {
                    Files.copy(zis, targetExe.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                }
            }
        }

        tempZip.delete();
        System.out.println("Extracted successfully.");
    }

    private void startOllamaDaemon(String executablePath, String enginePort) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.environment().put("OLLAMA_HOST", "0.0.0.0:" + enginePort);
            builder.environment().put("OLLAMA_MODELS", System.getProperty("user.home") + "/.coltranai/models");
    
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

    public void stopOllamaDaemon() {
        if(ollamaProcess != null && ollamaProcess.isAlive()) {
            ollamaProcess.destroy();
            System.out.println("Ollama AI Engine shut down successfully.");
        }
    }
    
}

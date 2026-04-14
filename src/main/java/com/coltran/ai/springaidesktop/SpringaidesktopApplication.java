package com.coltran.ai.springaidesktop;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;

import com.coltran.ai.springaidesktop.infrastructure.desktop.OllamaOrchestrator;

@SpringBootApplication(exclude = {JdbcRepositoriesAutoConfiguration.class})
public class SpringaidesktopApplication {

	public static void main(String[] args) {

		try {
			Path appDirectory = Paths.get(System.getProperty("user.home"), ".coltranai");

			Path modelsDirectory = appDirectory.resolve("models");

			if(Files.notExists(appDirectory)) {
				Files.createDirectories(appDirectory);
				System.out.println("Created application directory at: " + appDirectory.toAbsolutePath());
			}

			if(Files.notExists(modelsDirectory)) {
				Files.createDirectories(modelsDirectory);
				System.out.println("Created models directory at: " + modelsDirectory.toAbsolutePath());
			}
		} catch (Exception e) {
			System.err.println("Fatal error: Could not create app directories - " + e.getMessage());
			System.exit(1);
		}

		SpringApplication app = new SpringApplication(SpringaidesktopApplication.class);
		app.addListeners(new OllamaOrchestrator());
		app.run(args);
	}

}

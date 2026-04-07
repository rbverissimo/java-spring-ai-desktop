package com.coltran.ai.springaidesktop;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;

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

		SpringApplication.run(SpringaidesktopApplication.class, args);
	}

}

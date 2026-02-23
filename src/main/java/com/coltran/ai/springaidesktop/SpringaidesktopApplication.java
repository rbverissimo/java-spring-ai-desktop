package com.coltran.ai.springaidesktop;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringaidesktopApplication {

	public static void main(String[] args) {

		try {
			Path appDirectory = Paths.get(System.getProperty("user.home"), ".coltranai");

			if(Files.notExists(appDirectory)){
				Files.createDirectories(appDirectory);
				System.out.println("Created application directory at: " + appDirectory.toAbsolutePath());
			}
		} catch (Exception e) {
			System.err.println("Fatal error: Could not create app directory - " + e.getMessage());
			System.exit(1);
		}

		SpringApplication.run(SpringaidesktopApplication.class, args);
	}

}

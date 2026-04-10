package com.coltran.ai.springaidesktop;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import java.nio.file.Files;


@SpringBootTest
class SpringaidesktopApplicationTests {

	@Test
	void contextLoads() {}

	@Test
	void shouldCreateDirectoriesSuccesfully(@TempDir Path tempDir) {
		String originalName = System.getProperty("user.home");
		System.setProperty("user.home", tempDir.toAbsolutePath().toString()); 

		try {

			SpringaidesktopApplication.main(new String[]{"--spring.main.web-application-type=none"});

			Path expectedAppDir = tempDir.resolve(".coltranai");
			Path expectedModelsDir = expectedAppDir.resolve("models");

			assertTrue(Files.exists(expectedAppDir), "The .coltranai application directory should have been created.");
            assertTrue(Files.exists(expectedModelsDir), "The nested models directory should have been created.");

		} finally {
			System.setProperty("user.home", originalName);
		}
	}

}

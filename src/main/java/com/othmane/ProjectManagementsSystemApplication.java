package com.othmane;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectManagementsSystemApplication {
	public static void main(String[] args) {
		// Load .env file from project root, with fallback to system environment variables
		Dotenv dotenv = Dotenv.configure()
				.directory(".") // Look for .env in project root
				.ignoreIfMissing() // Don't fail if .env is missing
				.load();

		// Set system properties from .env, but only if not already set
		dotenv.entries().forEach(entry -> {
			if (System.getenv(entry.getKey()) == null && System.getProperty(entry.getKey()) == null) {
				System.setProperty(entry.getKey(), entry.getValue());
			}
		});

		SpringApplication.run(ProjectManagementsSystemApplication.class, args);
	}
}

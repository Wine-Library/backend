package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.example.config.DatabaseInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ApplicationContextTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.6");

    @TempDir
    static Path uploadDir;

    @MockBean
    private DatabaseInitializer databaseInitializer;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("jwt.secret", () -> "test-secret-key-for-context-load-please-do-not-reuse-in-prod");
        registry.add("spring.mail.username", () -> "test@example.com");
        registry.add("spring.mail.password", () -> "test-password");
        registry.add("wines.api.key", () -> "test-key");
        registry.add("app.frontend.url", () -> "http://localhost:3000");
        registry.add("upload.dir", () -> uploadDir.toString());
    }

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoadsAgainstARealDatabase() {
        assertThat(context).isNotNull();
    }
}

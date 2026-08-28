package org.example;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.sql.Connection;
import java.sql.Statement;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
class LiquibaseAgainstExistingDataTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.6");

    @Test
    void migrationsSucceedAgainstUsersCreatedBeforeLaterColumnsExisted() throws Exception {
        try (Connection connection = postgres.createConnection("")) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            new Liquibase(
                    "db/changelog/db.changelog-test-legacy-state.yaml",
                    new ClassLoaderResourceAccessor(),
                    database)
                    .update("dev");

            connection.setAutoCommit(true);
            try (Statement statement = connection.createStatement()) {
                statement.execute(
                        "INSERT INTO users (email, password, is_deleted, older_than_eighteen, enabled) "
                        + "VALUES ('legacy.user@example.com', 'irrelevant-hash', false, true, true)");
            }

            Liquibase fullChain = new Liquibase(
                    "db/changelog/db.changelog-master.yaml",
                    new ClassLoaderResourceAccessor(),
                    database);

            assertThatCode(() -> fullChain.update(""))
                    .as("later migrations must not assume every existing row already has "
                            + "values for columns they are about to make NOT NULL")
                    .doesNotThrowAnyException();
        }
    }
}

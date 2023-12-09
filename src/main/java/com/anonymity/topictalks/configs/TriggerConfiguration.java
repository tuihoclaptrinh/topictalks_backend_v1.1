package com.anonymity.topictalks.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

/**
 * @author de140172 - author
 * @version 1.2 - version of software
 * - Package Name: com.anonymity.topictalks.configs
 * - Created At: 25-10-2023 09:04:35
 * @since 1.0 - version of class
 */
@Configuration
public class TriggerConfiguration {

    @Bean
    public CommandLineRunner runScripts(JdbcTemplate jdbcTemplate) {
        return args -> {
            executeScript(jdbcTemplate, "drop_trigger.sql");
            executeScript(jdbcTemplate, "drop_procedure.sql");
            executeScript(jdbcTemplate, "drop_update_procedure.sql");
            executeScript(jdbcTemplate, "drop_insert_age_user_trigger.sql");
            executeScript(jdbcTemplate, "create_trigger.sql");
            executeScript(jdbcTemplate, "update_is_ban_procedure.sql");
            executeScript(jdbcTemplate, "procedure_verify_account.sql");
            executeScript(jdbcTemplate, "update_age_user_trigger.sql");
            executeScript(jdbcTemplate, "insert_age_user_procedure.sql");

        };
    }

    private void executeScript(JdbcTemplate jdbcTemplate, String scriptFileName) {
        Resource resource = new ClassPathResource(scriptFileName);

        try {
            String sqlScript = new String(resource.getInputStream().readAllBytes());
            jdbcTemplate.execute(sqlScript);
            System.out.println("Executed: " + scriptFileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to execute: " + scriptFileName);
        }
    }

}

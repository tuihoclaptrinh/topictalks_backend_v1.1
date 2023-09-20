package com.anonymity.topictalks.configs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configs
 * - Created At: 20-09-2023 08:38:19
 * @since 1.0 - version of class
 */

@Configuration
public class QuerydslConfiguration {

    @Bean
    public JPAQueryFactory queryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }

}

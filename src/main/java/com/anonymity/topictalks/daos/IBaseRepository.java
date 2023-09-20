package com.anonymity.topictalks.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.daos
 * - Created At: 20-09-2023 07:56:12
 * @since 1.0 - version of class
 */

@NoRepositoryBean
public interface IBaseRepository<T, ID> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
}

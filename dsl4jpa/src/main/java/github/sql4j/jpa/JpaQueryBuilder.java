package github.sql4j.jpa;

import github.sql4j.dsl.support.builder.query.AbstractQueryBuilder;

import javax.persistence.EntityManager;

public class JpaQueryBuilder extends AbstractQueryBuilder {

    public JpaQueryBuilder(EntityManager entityManager) {
        super(new JpaTypeQueryFactory(entityManager));
    }

}

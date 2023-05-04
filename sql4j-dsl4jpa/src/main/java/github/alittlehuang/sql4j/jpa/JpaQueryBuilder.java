package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.support.builder.AbstractQueryBuilder;
import jakarta.persistence.EntityManager;

public class JpaQueryBuilder extends AbstractQueryBuilder {

    public JpaQueryBuilder(EntityManager entityManager) {
        super(new JpaTypeQueryFactory(entityManager));
    }

}

package github.sql4j.jpa;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;

import jakarta.persistence.EntityManager;

public class JpaTypeQueryFactory implements TypeQueryFactory {

    private final EntityManager entityManager;

    public JpaTypeQueryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public <T> ResultBuilder<T> getEntityResultQuery(StructuredQuery criteriaQuery, Class<T> type) {
        return new JpaEntityResultBuilder<>(entityManager, type, criteriaQuery);
    }

    @Override
    public <T, R> ResultBuilder<R> getProjectionQuery(StructuredQuery criteriaQuery, Class<T> type, Class<R> projectionType) {
        return new ProjectionResultBuilder<>(this, criteriaQuery, type, projectionType);
    }

    @Override
    public ResultBuilder<Object[]> getObjectsTypeQuery(StructuredQuery criteriaQuery, Class<?> type) {
        return new JpaObjectsResultBuilder<>(entityManager, type, criteriaQuery);
    }


}

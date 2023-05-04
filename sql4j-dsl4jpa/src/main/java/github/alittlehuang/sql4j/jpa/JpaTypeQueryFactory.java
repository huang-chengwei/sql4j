package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.ResultQueryFactory;
import github.alittlehuang.sql4j.dsl.support.builder.ProjectionResultBuilder;
import github.alittlehuang.sql4j.dsl.util.Tuple;
import jakarta.persistence.EntityManager;

public class JpaTypeQueryFactory implements ResultQueryFactory {

    private final EntityManager entityManager;

    public JpaTypeQueryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public <T> ResultBuilder<T> getEntityResultQuery(QuerySpecification criteriaQuery, Class<T> type) {
        return new JpaEntityResultBuilder<>(entityManager, type, criteriaQuery);
    }

    @Override
    public <T, R> ResultBuilder<R> getProjectionQuery(QuerySpecification criteriaQuery, Class<T> type, Class<R> projectionType) {
        return new ProjectionResultBuilder<>(this, criteriaQuery, type, projectionType);
    }

    @Override
    public ResultBuilder<Tuple> getObjectsTypeQuery(QuerySpecification criteriaQuery, Class<?> type) {
        return new JpaObjectsResultBuilder<>(entityManager, type, criteriaQuery);
    }


}

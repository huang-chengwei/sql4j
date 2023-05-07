package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.ResultBuilderFactory;
import github.alittlehuang.sql4j.dsl.support.builder.projection.ProjectionResultBuilder;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMetaProvider;
import github.alittlehuang.sql4j.dsl.util.Tuple;

import javax.persistence.EntityManager;

public class JpaTypeQueryFactory implements ResultBuilderFactory {

    private final EntityManager entityManager;
    private final ProjectionMetaProvider metaProvider;

    public JpaTypeQueryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
        metaProvider = null;
    }

    public JpaTypeQueryFactory(EntityManager entityManager, ProjectionMetaProvider metaProvider) {
        this.entityManager = entityManager;
        this.metaProvider = metaProvider;
    }

    @Override
    public <T> ResultBuilder<T> getEntityResultQuery(QuerySpecification criteriaQuery, Class<T> type) {
        return new JpaEntityResultBuilder<>(entityManager, type, criteriaQuery);
    }

    @Override
    public <T, R> ResultBuilder<R> getProjectionQuery(QuerySpecification spec, Class<T> type, Class<R> projectionType) {
        return new ProjectionResultBuilder<>(this, spec, type, projectionType, metaProvider);
    }

    @Override
    public ResultBuilder<Tuple> getObjectsTypeQuery(QuerySpecification criteriaQuery, Class<?> type) {
        return new JpaObjectsResultBuilder<>(entityManager, type, criteriaQuery);
    }


}

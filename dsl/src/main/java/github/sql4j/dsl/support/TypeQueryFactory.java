package github.sql4j.dsl.support;

import github.sql4j.dsl.builder.ResultBuilder;

public interface TypeQueryFactory {

    <T> ResultBuilder<T> getEntityResultQuery(StructuredQuery criteriaQuery, Class<T> type);

    <T, R> ResultBuilder<R> getProjectionQuery(StructuredQuery criteriaQuery,
                                               Class<T> type,
                                               Class<R> projectionType);

    ResultBuilder<Object[]> getObjectsTypeQuery(StructuredQuery criteriaQuery, Class<?> type);

}

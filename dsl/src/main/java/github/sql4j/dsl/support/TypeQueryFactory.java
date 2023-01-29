package github.sql4j.dsl.support;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.util.Tuple;

public interface TypeQueryFactory {

    <T> ResultBuilder<T> getEntityResultQuery(StructuredQuery criteriaQuery, Class<T> type);

    <T, R> ResultBuilder<R> getProjectionQuery(StructuredQuery criteriaQuery,
                                               Class<T> type,
                                               Class<R> projectionType);

    ResultBuilder<Tuple> getObjectsTypeQuery(StructuredQuery criteriaQuery, Class<?> type);

}

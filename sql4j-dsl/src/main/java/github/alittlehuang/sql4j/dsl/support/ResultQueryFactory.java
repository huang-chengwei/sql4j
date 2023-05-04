package github.alittlehuang.sql4j.dsl.support;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.util.Tuple;

public interface ResultQueryFactory {

    <T> ResultBuilder<T> getEntityResultQuery(QuerySpecification criteriaQuery, Class<T> type);

    <T, R> ResultBuilder<R> getProjectionQuery(QuerySpecification criteriaQuery,
                                               Class<T> type,
                                               Class<R> projectionType);

    ResultBuilder<Tuple> getObjectsTypeQuery(QuerySpecification criteriaQuery, Class<?> type);

}

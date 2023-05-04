package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.Projection;
import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.builder.QuerySupport;

public class DefaultProjection<T> implements Projection {
    private final QuerySupport<T> structured;

    public DefaultProjection(QuerySupport<T> structured) {
        this.structured = structured;
    }

    @Override
    public <R> ResultBuilder<R> projected(Class<R> projectionType) {
        return structured.getTypeQueryFactory().getProjectionQuery(structured.buildQuerySpec(), structured.getType(), projectionType);
    }
}

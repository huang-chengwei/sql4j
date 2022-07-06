package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.QueryBuilder;
import github.sql4j.dsl.builder.Query;
import github.sql4j.dsl.support.TypeQueryFactory;

public abstract class AbstractQueryBuilder implements QueryBuilder {

    private final TypeQueryFactory typeQueryFactory;

    public AbstractQueryBuilder(TypeQueryFactory typeQueryFactory) {
        this.typeQueryFactory = typeQueryFactory;
    }

    @Override
    public <T> Query<T> query(Class<T> type) {
        return new QueryImpl<>(typeQueryFactory, type, null);
    }

}

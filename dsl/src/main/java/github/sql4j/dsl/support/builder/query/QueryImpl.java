package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.*;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class QueryImpl<T> extends AbstractResult<T> implements Query<T> {

    public QueryImpl(TypeQueryFactory typeQueryFactory, Class<T> entityType, StructuredQuery criteriaQuery) {
        super(typeQueryFactory, entityType, criteriaQuery);
    }

    @Delegate
    @Override
    protected @NotNull WhereImpl<T, WhereBuilder<T>> getWhere() {
        return super.getWhere();
    }

    @Delegate
    @Override
    protected @NotNull Fetch<T, FetchBuilder<T>> getFetch() {
        return super.getFetch();
    }

    @Delegate
    @Override
    protected @NotNull Sort<T, WhereBuilder<T>> getSortable() {
        return super.getSortable();
    }

    @Delegate
    @Override
    protected @NotNull GroupBy<T, GroupByBuilder<T>> getGroupBy() {
        return super.getGroupBy();
    }

    @Delegate
    @Override
    protected @NotNull Select<T, SelectBuilder<T>> getSelectable() {
        return super.getSelectable();
    }

    @Delegate
    @Override
    protected @NotNull AggregateSelect<T, AggregateSelectBuilder<T>> getAggregateSelectable() {
        return super.getAggregateSelectable();
    }

    @Delegate
    @Override
    protected ResultBuilder<T> getTypeQuery() {
        return super.getTypeQuery();
    }

}
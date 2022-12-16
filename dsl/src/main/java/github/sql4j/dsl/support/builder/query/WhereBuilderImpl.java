package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.*;
import github.sql4j.dsl.builder.FetchBuilder;
import github.sql4j.dsl.builder.GroupByBuilder;
import github.sql4j.dsl.builder.SelectBuilder;
import github.sql4j.dsl.builder.WhereBuilder;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class WhereBuilderImpl<T> extends AbstractResult<T> implements WhereBuilder<T> {
    public WhereBuilderImpl(TypeQueryFactory typeQueryFactory, Class<T> entityType, StructuredQuery criteriaQuery) {
        super(typeQueryFactory, entityType, criteriaQuery);
    }

    @Delegate
    protected @NotNull PredicateAssembler<T, WhereBuilder<T>> getWereBuilderRestrictionBuilder() {
        return super.getWereBuilderRestrictionBuilder();
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
    protected ResultBuilder<T> getTypeQuery() {
        return super.getTypeQuery();
    }

}

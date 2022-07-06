package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.AggregateSelect;
import github.sql4j.dsl.builder.GroupBy;
import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.builder.AggregateSelectBuilder;
import github.sql4j.dsl.builder.GroupByBuilder;
import github.sql4j.dsl.builder.SelectBuilder;
import github.sql4j.dsl.builder.Where;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class AggregateSelectBuilderImpl<T> extends AbstractResult<T> implements AggregateSelectBuilder<T> {

    public AggregateSelectBuilderImpl(TypeQueryFactory typeQueryFactory, Class<T> entityType, StructuredQuery criteriaQuery) {
        super(typeQueryFactory, entityType, criteriaQuery);
    }

    @Delegate
    @Override
    protected @NotNull Where<T, SelectBuilder<T>> getObjectsWhereable() {
        return super.getObjectsWhereable();
    }

    @Delegate
    @Override
    protected @NotNull GroupBy<T, GroupByBuilder<T>> getGroupable() {
        return super.getGroupable();
    }

    @Delegate
    @Override
    protected @NotNull AggregateSelect<T, AggregateSelectBuilder<T>> getAggregateSelectable() {
        return super.getAggregateSelectable();
    }

    @Delegate
    @Override
    protected ResultBuilder<Object[]> getObjectsTypeQuery() {
        return super.getObjectsTypeQuery();
    }
}

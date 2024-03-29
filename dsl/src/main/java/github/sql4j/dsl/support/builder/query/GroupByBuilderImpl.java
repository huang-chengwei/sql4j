package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.AggregateSelect;
import github.sql4j.dsl.builder.GroupBy;
import github.sql4j.dsl.builder.Select;
import github.sql4j.dsl.builder.AggregateSelectBuilder;
import github.sql4j.dsl.builder.GroupByBuilder;
import github.sql4j.dsl.builder.SelectBuilder;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class GroupByBuilderImpl<T> extends AbstractResult<T> implements GroupByBuilder<T> {

    public GroupByBuilderImpl(TypeQueryFactory typeQueryFactory,
                              Class<T> entityType,
                              StructuredQuery criteriaQuery) {
        super(typeQueryFactory, entityType, criteriaQuery);
    }

    @Delegate
    @Override
    protected @NotNull Select<T, SelectBuilder<T>> getSelectable() {
        return super.getSelectable();
    }

    @Delegate
    @Override
    protected @NotNull GroupBy<T, GroupByBuilder<T>> getGroupBy() {
        return super.getGroupBy();
    }

    @Delegate
    @Override
    protected @NotNull AggregateSelect<T, AggregateSelectBuilder<T>> getAggregateSelectable() {
        return super.getAggregateSelectable();
    }
}

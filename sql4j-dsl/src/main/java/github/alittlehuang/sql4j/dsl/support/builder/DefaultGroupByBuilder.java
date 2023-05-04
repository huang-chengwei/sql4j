package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.util.Tuple;
import lombok.experimental.Delegate;

public class DefaultGroupByBuilder<T> implements GroupByBuilder<T> {
    private final QuerySupport<T> support;

    public DefaultGroupByBuilder(QuerySupport<T> support) {
        this.support = support;
    }

    @Delegate
    GroupBy<T, GroupByBuilder<T>> groupBy() {
        return support.buildGroupBy(support::groupByToGroupByBuilder);
    }

    @Delegate
    Select<T, SelectBuilder<T>> select() {
        return support.buildSelect(support::selectToSelectBuilder);
    }

    @Delegate
    AggregateSelect<T, AggregateSelectBuilder<T>> aggregateSelect() {
        return support.buildAggregateSelect(support::aggregateSelectToAggregateSelectBuilder);
    }

    @Delegate
    ResultBuilder<Tuple> resultBuilder() {
        return support.getTupleResultBuilder();
    }
}

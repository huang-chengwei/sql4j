package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.util.Tuple;
import lombok.experimental.Delegate;

public class DefaultAggregateSelectBuilder<T> implements AggregateSelectBuilder<T> {
    private final QuerySupport<T> support;

    public DefaultAggregateSelectBuilder(QuerySupport<T> support) {
        this.support = support;
    }


    @Delegate
    Where<T, SelectBuilder<T>> where() {
        return support.buildWhere(support::whereToSelectBuilder);
    }

    @Delegate
    GroupBy<T, GroupByBuilder<T>> groupBy() {
        return support.buildGroupBy(support::groupByToGroupByBuilder);
    }

    @Delegate
    AggregateSelect<T, AggregateSelectBuilder<T>> aggregateSelect() {
        return support.buildAggregateSelect(support::aggregateSelectToAggregateSelectBuilder);
    }

    @Delegate
    Select<T, SelectBuilder<T>> select() {
        return support.buildSelect(support::selectToSelectBuilder);
    }


    @Delegate
    ResultBuilder<Tuple> resultBuilder() {
        return support.getTupleResultBuilder();
    }
}

package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.util.Tuple;
import lombok.experimental.Delegate;

public class DefaultSelectBuilder<T> implements SelectBuilder<T> {

    private final QuerySupport<T> support;

    public DefaultSelectBuilder(QuerySupport<T> support) {
        this.support = support;
    }


    @Delegate
    Where<T, SelectBuilder<T>> where() {
        return support.buildWhere(support::whereToSelectBuilder);
    }

    @Delegate
    OrderBy<T, SelectBuilder<T>> orderBy() {
        return support.buildOrderBy(order -> new DefaultSelectBuilder<>(support.addOrderBy(order)));
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

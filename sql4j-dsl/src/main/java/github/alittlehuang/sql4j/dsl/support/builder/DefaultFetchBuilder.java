package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.support.builder.operator.FlowPredicateOperate;
import lombok.experimental.Delegate;

public class DefaultFetchBuilder<T> implements FetchBuilder<T> {
    private final QuerySupport<T> support;

    public DefaultFetchBuilder(QuerySupport<T> support) {
        this.support = support;
    }


    @Delegate
    PredicateCombiner<T, FetchBuilder<T>> predicateCombiner() {
        return support.buildWhere((expression, operator) -> {
            Expression where = FlowPredicateOperate
                    .operate(support.whereClause(), operator, expression);
            QuerySupport<T> updated = support.updateWhere(where);
            return new DefaultFetchBuilder<>(updated);
        });
    }

    @Delegate
    Fetch<T, FetchBuilder<T>> fetch() {
        return support.buildFetch(support::fetchToFetchBuilder);

    }

    @Delegate
    OrderBy<T, FetchBuilder<T>> orderBy() {
        return support.buildOrderBy(order -> new DefaultFetchBuilder<>(support.addOrderBy(order)));
    }

    @Delegate
    ResultBuilder<T> resultBuilder() {
        return support.getResultBuilder();
    }

}

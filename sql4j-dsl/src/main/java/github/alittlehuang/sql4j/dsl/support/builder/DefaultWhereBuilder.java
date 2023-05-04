package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.support.builder.operator.FlowPredicateOperate;
import lombok.experimental.Delegate;

public class DefaultWhereBuilder<T> implements WhereBuilder<T> {

    private final QuerySupport<T> support;

    public DefaultWhereBuilder(QuerySupport<T> support) {
        this.support = support;
    }

    @Delegate
    PredicateCombiner<T, WhereBuilder<T>> predicateCombiner() {
        return support.buildWhere((expression, operator) -> {
            Expression where = FlowPredicateOperate
                    .operate(support.whereClause(), operator, expression);
            return new DefaultWhereBuilder<>(support.updateWhere(where));
        });
    }

    @Delegate
    Fetch<T, FetchBuilder<T>> fetch() {
        return support.buildFetch(support::fetchToFetchBuilder);
    }

    @Delegate
    OrderBy<T, WhereBuilder<T>> orderBy() {
        return support.buildOrderBy(order -> new DefaultWhereBuilder<>(support.addOrderBy(order)));
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
    Projection projection() {
        return support.buildProjection();
    }

    @Delegate
    ResultBuilder<T> resultBuilder() {
        return support.getResultBuilder();
    }

}

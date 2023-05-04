package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.support.builder.operator.FlowPredicateOperate;
import lombok.experimental.Delegate;

public class DefaultQuery<T> implements Query<T> {

    private final QuerySupport<T> support;

    public DefaultQuery(QuerySupport<T> support) {
        this.support = support;
    }

    @Delegate
    Where<T, WhereBuilder<T>> where() {
        return support.buildWhere(expression -> {
            Expression where = FlowPredicateOperate.and(support.whereClause(), expression);
            return new DefaultWhereBuilder<>(support.updateWhere(where));
        });
    }

    @Delegate
    Fetch<T, FetchBuilder<T>> fetch() {
        return support.buildFetch(support::fetchToFetchBuilder);
    }


    @Delegate
    OrderBy<T, WhereBuilder<T>> orderBy() {
        return support.buildOrderBy(support::orderByToWhereBuilder);
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
    Projection projection() {
        return support.buildProjection();
    }

    @Delegate
    ResultBuilder<T> resultBuilder() {
        return support.getResultBuilder();
    }


}

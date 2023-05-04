package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.SortSpecification;
import github.alittlehuang.sql4j.dsl.support.Configure;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.ResultQueryFactory;
import github.alittlehuang.sql4j.dsl.support.builder.operator.*;
import github.alittlehuang.sql4j.dsl.util.Array;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class QuerySupport<T> {

    private final QuerySpecification spec;
    private final Class<T> type;
    private final ResultQueryFactory typeQueryFactory;
    private final Configure config;

    public QuerySupport(QuerySpecification spec,
                        Class<T> type,
                        ResultQueryFactory typeQueryFactory,
                        Configure config) {
        this.spec = Objects.requireNonNull(spec);
        this.type = Objects.requireNonNull(type);
        this.typeQueryFactory = Objects.requireNonNull(typeQueryFactory);
        this.config = Objects.requireNonNull(config);
    }

    public static <T> @NotNull QuerySupport<T> of(Class<T> type, ResultQueryFactory factory) {
        return of(type, factory, null);
    }

    public static <T> @NotNull QuerySupport<T> of(Class<T> type, ResultQueryFactory factory, Configure config) {
        return new QuerySupport<>(QuerySpecification.EMPTY, type, factory, config);
    }

    public QuerySpecification buildQuerySpec() {
        UnaryOperator<QuerySupport<T>> postProcess = config.postProcessBeforeBuildQuerySpec();
        if (postProcess != null) {
            return postProcess.apply(this).spec;
        }
        return spec;
    }


    public <B> DefaultPredicateCombiner<T, B> buildWhere(BiFunction<Expression, Operator, B> mapper) {
        return new DefaultPredicateCombiner<>(mapper);
    }

    public <B> DefaultWhere<T, B> buildWhere(Function<Expression, B> mapper) {
        return new DefaultWhere<>(mapper);
    }

    public <B> DefaultFetch<T, B> buildFetch(Function<PathExpression, B> mapper) {
        return new DefaultFetch<>(mapper);
    }

    public <B> DefaultOrderBy<T, B> buildOrderBy(Function<SortSpecification, B> mapper) {
        return new DefaultOrderBy<>(mapper);
    }

    public <B> DefaultGroupBy<T, B> buildGroupBy(Function<List<Expression>, B> mapper) {
        return new DefaultGroupBy<>(mapper);
    }

    public <B> DefaultSelect<T, B> buildSelect(Function<List<Expression>, B> mapper) {
        return new DefaultSelect<>(mapper);
    }

    public <B> DefaultAggregateSelect<T, B> buildAggregateSelect(Function<Expression, B> mapper) {
        return new DefaultAggregateSelect<>(mapper);
    }

    public DefaultProjection<T> buildProjection() {
        return new DefaultProjection<>(this);
    }

    public DefaultResultBuilder<T> getResultBuilder() {
        return new DefaultResultBuilder<>(this);
    }

    public TupleResultBuilder getTupleResultBuilder() {
        return new TupleResultBuilder(this);
    }

    @NotNull
    public DefaultSelectBuilder<T> selectToSelectBuilder(List<Expression> expressions) {
        return new DefaultSelectBuilder<>(updateSelect(spec.selectClause().join(expressions)));
    }

    @NotNull
    public DefaultGroupByBuilder<T> groupByToGroupByBuilder(List<Expression> expressions) {
        Array<Expression> groupBy = spec.groupByClause().join(expressions);
        return new DefaultGroupByBuilder<>(updateGroupBy(groupBy));
    }

    @NotNull
    public DefaultFetchBuilder<T> fetchToFetchBuilder(PathExpression expression) {
        Array<PathExpression> fetch = spec.fetchClause().join(expression);
        return new DefaultFetchBuilder<>(updateFetch(fetch));
    }

    public DefaultAggregateSelectBuilder<T> aggregateSelectToAggregateSelectBuilder(Expression expression) {
        QuerySupport<T> updated = updateSelect(spec.selectClause().join(expression));
        return new DefaultAggregateSelectBuilder<>(updated);
    }

    public DefaultSelectBuilder<T> whereToSelectBuilder(Expression expression) {
        Expression and = FlowPredicateOperate.and(spec.whereClause(), expression);
        return new DefaultSelectBuilder<>(updateWhere(and));
    }

    @NotNull
    public DefaultWhereBuilder<T> orderByToWhereBuilder(SortSpecification order) {
        return new DefaultWhereBuilder<>(addOrderBy(order));
    }

    //

    public QuerySupport<T> updateWhere(Expression where) {
        return updateSpec(spec.updateWhere(where));
    }

    public QuerySupport<T> updateFetch(Array<PathExpression> fetch) {
        return updateSpec(spec.updateFetch(fetch));
    }

    public QuerySupport<T> addOrderBy(SortSpecification orderBy) {
        return updateOrderBy(spec.sortSpec().join(orderBy));
    }

    public QuerySupport<T> updateOrderBy(Array<SortSpecification> orderBy) {
        return updateSpec(spec.updateSort(orderBy));
    }

    public QuerySupport<T> updateGroupBy(Array<Expression> groupBy) {
        return updateSpec(spec.updateGroupBy(groupBy));
    }

    public QuerySupport<T> updateSelect(Array<Expression> select) {
        return updateSpec(spec.updateSelect(select));
    }

    @NotNull
    private QuerySupport<T> updateSpec(QuerySpecification spec) {
        return new QuerySupport<>(spec, type, typeQueryFactory, config);
    }

    public Class<T> getType() {
        return type;
    }

    public ResultQueryFactory getTypeQueryFactory() {
        return typeQueryFactory;
    }

    public Expression whereClause() {
        return spec.whereClause();
    }
}

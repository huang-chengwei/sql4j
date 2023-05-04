package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.Predicate;
import github.alittlehuang.sql4j.dsl.expression.path.*;

import java.util.function.BiFunction;

public class DefaultPredicateCombiner<T, B> extends DefaultWhere<T, B> implements PredicateCombiner<T, B> {
    protected final BiFunction<Expression, Operator, B> builder;

    public DefaultPredicateCombiner(BiFunction<Expression, Operator, B> builder) {
        super(expression -> builder.apply(expression, Operator.AND));
        this.builder = builder;
    }

    @Override
    public <R extends Persistable> PathBuilder<T, R, B> and(EntityGetter<T, R> attribute) {
        return new DefaultPathBuilder<>(buildAnd(attribute));
    }

    @Override
    public <R extends Persistable> PathBuilder<T, R, B> or(EntityGetter<T, R> attribute) {
        return new DefaultPathBuilder<>(buildOr(attribute));
    }

    @Override
    public <R> PredicateOperator<T, R, B> and(ColumnGetter<T, R> attribute) {
        return new DefaultPredicateOperator<>(buildAnd(attribute));
    }

    @Override
    public <R> PredicateOperator<T, R, B> or(ColumnGetter<T, R> attribute) {
        return new DefaultPredicateOperator<>(buildOr(attribute));
    }

    @Override
    public <R extends Number & Comparable<?>> NumberOperator<T, R, B> and(NumberGetter<T, R> attribute) {
        return new DefaultNumberOperator<>(buildAnd(attribute));
    }

    @Override
    public <R extends Number & Comparable<?>> NumberOperator<T, R, B> or(NumberGetter<T, R> attribute) {
        return new DefaultNumberOperator<>(buildOr(attribute));
    }

    @Override
    public <R extends Comparable<?>> ComparableOperator<T, R, B> and(ComparableGetter<T, R> attribute) {
        return new DefaultComparableOperator<>(buildAnd(attribute));
    }

    @Override
    public <R extends Comparable<?>> ComparableOperator<T, R, B> or(ComparableGetter<T, R> attribute) {
        return new DefaultComparableOperator<>(buildOr(attribute));
    }

    @Override
    public StringOperator<T, B> and(StringGetter<T> attribute) {
        return new DefaultStringOperator<>(buildAnd(attribute));
    }

    @Override
    public StringOperator<T, B> or(StringGetter<T> attribute) {
        return new DefaultStringOperator<>(buildOr(attribute));
    }

    @Override
    public B and(Predicate<T> predicate) {
        return builder.apply(predicate.expression(), Operator.AND);
    }

    @Override
    public B or(Predicate<T> predicate) {
        return builder.apply(predicate.expression(), Operator.OR);
    }


    private DataAction<PathExpression, Expression, B> buildAnd(ColumnGetter<?, ?> column) {
        return new DataAction<>(column.expression(), this::buildAnd);
    }

    private B buildAnd(Expression expression) {
        return builder.apply(expression, Operator.AND);
    }

    private DataAction<PathExpression, Expression, B> buildOr(ColumnGetter<?, ?> column) {
        return new DataAction<>(column.expression(), this::buildOr);
    }

    private B buildOr(Expression expression) {
        return builder.apply(expression, Operator.OR);
    }

}

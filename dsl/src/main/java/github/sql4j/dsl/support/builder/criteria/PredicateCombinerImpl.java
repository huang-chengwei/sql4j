package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.PredicateCombiner;
import github.sql4j.dsl.builder.ComparablePredicate;
import github.sql4j.dsl.builder.NumberPredicate;
import github.sql4j.dsl.builder.BasePredicate;
import github.sql4j.dsl.builder.StringPredicate;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.Predicate;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.Entity;
import github.sql4j.dsl.expression.path.PathBuilder;
import github.sql4j.dsl.expression.path.attribute.*;
import github.sql4j.dsl.support.builder.component.*;

import java.util.function.Function;

public class PredicateCombinerImpl<T, BUILDER> implements PredicateCombiner<T, BUILDER> {

    protected final SubPredicateArray expression;
    protected final Function<Expression<Boolean>, BUILDER> mapper;

    public PredicateCombinerImpl(Expression<Boolean> expression,
                                 Function<Expression<Boolean>, BUILDER> mapper) {
        this.expression = SubPredicateArray.fromExpression(expression);
        this.mapper = mapper;
    }

    @Override
    public <R extends Entity> PathBuilder<T, R, BUILDER> and(EntityAttribute<T, R> attribute) {
        return new PathBuilderImpl<>(AttributePath.exchange(attribute), Operator.AND, false, this::mapperNext);
    }

    @Override
    public <R extends Entity> PathBuilder<T, R, BUILDER> or(EntityAttribute<T, R> attribute) {
        return new PathBuilderImpl<>(AttributePath.exchange(attribute), Operator.OR, false, this::mapperNext);
    }

    public <R extends Entity> PathBuilder<T, R, BUILDER> andNot(EntityAttribute<T, R> attribute) {
        return new PathBuilderImpl<>(AttributePath.exchange(attribute), Operator.AND, true, this::mapperNext);
    }

    @Override
    public <R> BasePredicate<T, R, BUILDER> and(Attribute<T, R> attribute) {
        return new BasePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false, this::mapperNext);
    }

    @Override
    public <R> BasePredicate<T, R, BUILDER> or(Attribute<T, R> attribute) {
        return new BasePredicateImpl<>(AttributePath.exchange(attribute), Operator.OR, false, this::mapperNext);
    }

    public <R> BasePredicate<T, R, BUILDER> andNot(Attribute<T, R> attribute) {
        return new BasePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, true, this::mapperNext);
    }

    @Override
    public <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> and(NumberAttribute<T, R> attribute) {
        return new NumberPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false, this::mapperNext);
    }

    @Override
    public <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> or(NumberAttribute<T, R> attribute) {
        return new NumberPredicateImpl<>(AttributePath.exchange(attribute), Operator.OR, false, this::mapperNext);
    }

    public <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> andNot(NumberAttribute<T, R> attribute) {
        return new NumberPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, true, this::mapperNext);
    }

    @Override
    public <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> and(ComparableAttribute<T, R> attribute) {
        return new ComparablePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false, this::mapperNext);
    }

    @Override
    public <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> or(ComparableAttribute<T, R> attribute) {
        return new ComparablePredicateImpl<>(AttributePath.exchange(attribute), Operator.OR, false, this::mapperNext);
    }

    public <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> andNot(ComparableAttribute<T, R> attribute) {
        return new ComparablePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, true, this::mapperNext);
    }

    @Override
    public StringPredicate<T, BUILDER> and(StringAttribute<T> attribute) {
        return new StringPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false, this::mapperNext);
    }

    @Override
    public StringPredicate<T, BUILDER> or(StringAttribute<T> attribute) {
        return new StringPredicateImpl<>(AttributePath.exchange(attribute), Operator.OR, false, this::mapperNext);
    }

    public StringPredicate<T, BUILDER> andNot(StringAttribute<T> attribute) {
        return new StringPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, true, this::mapperNext);
    }

    @Override
    public BUILDER and(Predicate<T> predicate) {
        SubPredicate subPredicate = new SubPredicate(predicate, Operator.AND, false);
        return mapperNext(subPredicate);
    }

    @Override
    public BUILDER or(Predicate<T> predicate) {
        SubPredicate subPredicate = new SubPredicate(predicate, Operator.OR, false);
        return mapperNext(subPredicate);
    }

    protected BUILDER mapperNext(SubPredicate subPredicate) {
        Expression<Boolean> then = getBooleanExpression(subPredicate);
        return next(mapper.apply(then));
    }

    protected BUILDER next(BUILDER BUILDER) {
        return BUILDER;
    }

    private Expression<Boolean> getBooleanExpression(SubPredicate subPredicate) {
        Expression<Boolean> expression = subPredicate.getExpression();
        if (subPredicate.isNegate()) {
            expression = expression.then(Operator.NOT);
        }
        return this.expression == null
                ? expression
                : this.expression.then(subPredicate.getCombined(), expression);
    }


}

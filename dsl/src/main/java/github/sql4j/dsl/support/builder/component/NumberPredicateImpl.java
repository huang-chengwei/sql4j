package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.builder.NumberPredicate;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.NumberAttribute;

import java.util.function.Function;

public class NumberPredicateImpl<T, U extends Number & Comparable<?>, BUILDER>
        extends ComparablePredicateImpl<T, U, BUILDER>
        implements NumberPredicate<T, U, BUILDER> {

    public NumberPredicateImpl(Expression<U> exchange,
                               Operator combined,
                               boolean negate,
                               Function<SubPredicate, BUILDER> mapper) {
        super(exchange, combined, negate, mapper);
    }

    @Override
    public NumberPredicate<T, U, BUILDER> add(U v) {
        Expression<U> then = expression.then(Operator.ADD, v);
        return new NumberPredicateImpl<>(then, combined, negate, mapper);
    }

    @Override
    public NumberPredicate<T, U, BUILDER> subtract(U v) {
        Expression<U> then = expression.then(Operator.SUBTRACT, v);
        return new NumberPredicateImpl<>(then, combined, negate, mapper);
    }

    @Override
    public NumberPredicate<T, U, BUILDER> multiply(U v) {
        Expression<U> then = expression.then(Operator.MULTIPLY, v);
        return new NumberPredicateImpl<>(then, combined, negate, mapper);
    }

    @Override
    public NumberPredicate<T, U, BUILDER> divide(U v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.DIVIDE, v),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, U, BUILDER> mod(U v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.MOD, v),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, U, BUILDER> add(NumberAttribute<T, U> v) {
        return add(AttributePath.exchange(v));
    }

    @Override
    public NumberPredicate<T, U, BUILDER> subtract(NumberAttribute<T, U> v) {
        return subtract(AttributePath.exchange(v));

    }

    @Override
    public NumberPredicate<T, U, BUILDER> multiply(NumberAttribute<T, U> v) {
        return multiply(AttributePath.exchange(v));
    }

    @Override
    public NumberPredicate<T, U, BUILDER> divide(NumberAttribute<T, U> v) {
        return divide(AttributePath.exchange(v));
    }

    @Override
    public NumberPredicate<T, U, BUILDER> mod(NumberAttribute<T, U> v) {
        return mod(AttributePath.exchange(v));
    }

    @Override
    public NumberPredicate<T, U, BUILDER> add(Expression<U> v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.ADD, v),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, U, BUILDER> subtract(Expression<U> v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.SUBTRACT, v),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, U, BUILDER> multiply(Expression<U> v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.MULTIPLY, v),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, U, BUILDER> divide(Expression<U> v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.DIVIDE, v),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, U, BUILDER> mod(Expression<U> v) {
        return new NumberPredicateImpl<>(
                expression.then(Operator.MOD, v),
                combined,
                negate,
                mapper
        );
    }

}

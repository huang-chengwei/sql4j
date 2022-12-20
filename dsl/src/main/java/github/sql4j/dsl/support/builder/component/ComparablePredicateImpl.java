package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.builder.ComparablePredicate;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.ComparableAttribute;

import java.util.function.Function;

public class ComparablePredicateImpl<T, U extends Comparable<?>, BUILDER>
        extends AbstractExpressionBuilder<T, U, BUILDER>
        implements ComparablePredicate<T, U, BUILDER> {

    public ComparablePredicateImpl(Expression<U> exchange,
                                   Operator combined,
                                   boolean negate,
                                   Function<SubPredicate, BUILDER> mapper) {
        super(exchange, combined, negate, mapper);
    }

    @Override
    public BUILDER ge(ComparableAttribute<T, U> value) {
        Expression<U> exchange = AttributePath.exchange(value);
        return super.ge(exchange);
    }

    @Override
    public BUILDER gt(ComparableAttribute<T, U> value) {
        Expression<U> exchange = AttributePath.exchange(value);
        return super.gt(exchange);
    }

    @Override
    public BUILDER le(ComparableAttribute<T, U> value) {
        Expression<U> exchange = AttributePath.exchange(value);
        return super.le(exchange);
    }

    @Override
    public BUILDER between(ComparableAttribute<T, U> a, ComparableAttribute<T, U> b) {
        Expression<U> ea = AttributePath.exchange(a);
        Expression<U> eb = AttributePath.exchange(b);
        return super.between(ea, eb);
    }

    @Override
    public BUILDER lt(ComparableAttribute<T, U> value) {
        Expression<U> exchange = AttributePath.exchange(value);
        return super.lt(exchange);
    }

    @Override
    public ComparablePredicate<T, U, BUILDER> not() {
        return new ComparablePredicateImpl<>(expression, combined, !negate, mapper);
    }

}

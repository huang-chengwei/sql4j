package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.builder.NumberPredicate;
import github.sql4j.dsl.builder.StringPredicate;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.Operator;

import java.util.function.Function;

public class StringPredicateImpl<T, BUILDER>
        extends ComparablePredicateImpl<T, String, BUILDER>
        implements StringPredicate<T, BUILDER> {

    public StringPredicateImpl(SqlExpression<String> exchange,
                               Operator combined,
                               boolean negate,
                               Function<SubPredicate, BUILDER> mapper) {
        super(exchange, combined, negate, mapper);
    }

    @Override
    public BUILDER like(String value) {
        return next(Operator.LIKE, value);
    }

    @Override
    public StringPredicate<T, BUILDER> lower() {
        return new StringPredicateImpl<>(
                expression.then(Operator.LOWER),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public StringPredicate<T, BUILDER> upper() {
        return new StringPredicateImpl<>(
                expression.then(Operator.UPPER),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public StringPredicate<T, BUILDER> substring(int a, int b) {
        return new StringPredicateImpl<>(
                expression.then(Operator.SUBSTRING, a, b),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public StringPredicate<T, BUILDER> substring(int a) {
        return new StringPredicateImpl<>(
                expression.then(Operator.SUBSTRING, a),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public StringPredicate<T, BUILDER> trim() {
        return new StringPredicateImpl<>(
                expression.then(Operator.TRIM),
                combined,
                negate,
                mapper
        );
    }

    @Override
    public NumberPredicate<T, Integer, BUILDER> length() {
        return new NumberPredicateImpl<>(
                expression.then(Operator.LENGTH),
                combined,
                negate,
                mapper
        );
    }
}
package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.builder.BasePredicate;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.Operator;

import java.util.function.Function;

public class BasePredicateImpl<T, U, BUILDER>
        extends AbstractExpressionBuilder<T, U, BUILDER>
        implements BasePredicate<T, U, BUILDER> {

    public BasePredicateImpl(SqlExpression<U> expression,
                             Operator combined,
                             boolean negate,
                             Function<SubPredicate, BUILDER> mapper) {
        super(expression, combined, negate, mapper);
    }

    @Override
    public BasePredicate<T, U, BUILDER> not() {
        return new BasePredicateImpl<>(expression, combined, !negate, mapper);
    }
}

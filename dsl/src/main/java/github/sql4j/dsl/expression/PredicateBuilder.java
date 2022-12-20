package github.sql4j.dsl.expression;

import github.sql4j.dsl.support.builder.criteria.PredicateCombinerImpl;
import lombok.experimental.Delegate;

public class PredicateBuilder<T> extends PredicateCombinerImpl<T, Predicate.Builder<T>> implements Predicate.Builder<T> {

    public PredicateBuilder(Expression<Boolean> expression) {
        super(expression, PredicateBuilder::map);
    }

    private static <T> Predicate.Builder<T> map(Expression<Boolean> expression) {
        return new PredicateBuilder<>(expression);
    }

    @Delegate
    public Expression<Boolean> getExpression() {
        return expression;
    }

    @Override
    public Predicate<T> not() {
        Expression<Boolean> then = then(Operator.NOT);
        return new PredicateBuilder<>(then);
    }

}

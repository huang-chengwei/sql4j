package github.sql4j.dsl.expression;

import github.sql4j.dsl.support.builder.criteria.PredicateAssemblerImpl;
import lombok.experimental.Delegate;

public class PredicateBuilder<T> extends PredicateAssemblerImpl<T, Predicate.Builder<T>> implements Predicate.Builder<T> {

    public PredicateBuilder(SqlExpression<Boolean> expression) {
        super(expression, PredicateBuilder::map);
    }

    private static <T> Predicate.Builder<T> map(SqlExpression<Boolean> expression) {
        return new PredicateBuilder<>(expression);
    }

    @Delegate
    public SqlExpression<Boolean> getExpression() {
        return expression;
    }

    @Override
    public Predicate<T> not() {
        SqlExpression<Boolean> then = then(Operator.NOT);
        return new PredicateBuilder<>(then);
    }

}

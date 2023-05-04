package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.ExpressionSupplier;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.PredicateBuilder;

public class DefaultPredicateBuilder<T>
        extends DefaultPredicateCombiner<T, PredicateBuilder<T>>
        implements PredicateBuilder<T> {

    private final Expression combiner;

    public DefaultPredicateBuilder(ExpressionSupplier builder) {
        this(builder.expression());
    }

    public DefaultPredicateBuilder(Expression combiner) {
        super((expression, operator) -> {
            Expression where = FlowPredicateOperate
                    .operate(combiner, operator, expression);
            return new DefaultPredicateBuilder<>(where);
        });
        this.combiner = combiner;
    }

    @Override
    public Expression expression() {
        return combiner;
    }

    @Override
    public PredicateBuilder<T> not() {
        return new DefaultPredicateBuilder<>(combiner.operate(Operator.NOT));
    }

}

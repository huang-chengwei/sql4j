package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.PredicateOperator;
import github.alittlehuang.sql4j.dsl.expression.Expression;

public class DefaultPredicateOperator<T, U, BUILDER> extends AbstractPredicateOperator<T, U, BUILDER>  {


    public DefaultPredicateOperator(DataAction<? extends Expression, ? super Expression, BUILDER> dataBuilder) {
        super(dataBuilder);
    }

    @Override
    public PredicateOperator<T, U, BUILDER> not() {
        return new DefaultPredicateOperator<>(negateDataBuilder());
    }

}

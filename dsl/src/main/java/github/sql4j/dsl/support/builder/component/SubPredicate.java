package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;

public class SubPredicate extends SubExpression<Boolean> {

    public SubPredicate(Expression<Boolean> expression, Operator combined, boolean negate) {
        super(expression, combined, negate);
    }


}

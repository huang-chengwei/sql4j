package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.Operator;

public class SubPredicate extends SubExpression<Boolean> {

    public SubPredicate(SqlExpression<Boolean> expression, Operator combined, boolean negate) {
        super(expression, combined, negate);
    }


}

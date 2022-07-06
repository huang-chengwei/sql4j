package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.Operator;
import lombok.Getter;


@Getter
public class SubExpression<T> {

    protected final SqlExpression<T> expression;
    protected final Operator combined;
    protected final boolean negate;

    public SubExpression(SqlExpression<T> expression, Operator combined, boolean negate) {
        this.expression = expression;
        this.combined = combined;
        this.negate = negate;
    }

}

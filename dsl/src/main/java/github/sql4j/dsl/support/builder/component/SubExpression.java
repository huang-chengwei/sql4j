package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;
import lombok.Getter;


@Getter
public class SubExpression<T> {

    protected final Expression<T> expression;
    protected final Operator combined;
    protected final boolean negate;

    public SubExpression(Expression<T> expression, Operator combined, boolean negate) {
        this.expression = expression;
        this.combined = combined;
        this.negate = negate;
    }

}

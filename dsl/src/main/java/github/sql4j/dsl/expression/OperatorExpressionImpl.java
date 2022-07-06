package github.sql4j.dsl.expression;

import java.util.List;

public class OperatorExpressionImpl<T> implements OperatorExpression<T> {

    private final List<? extends SqlExpression<?>> expressions;
    private final Operator operator;

    public OperatorExpressionImpl(List<? extends SqlExpression<?>> expressions, Operator operator) {
        this.expressions = expressions;
        this.operator = operator;
    }

    @Override
    public List<? extends SqlExpression<?>> getExpressions() {
        return expressions;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

}

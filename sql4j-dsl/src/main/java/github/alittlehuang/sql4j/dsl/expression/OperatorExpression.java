package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.util.Array;

import java.util.Objects;

public final class OperatorExpression implements Expression {
    private final Array<Expression> expressions;
    private final Operator operator;

    public OperatorExpression(Array<Expression> expressions, Operator operator) {
        this.expressions = expressions;
        this.operator = operator;
    }

    public Array<Expression> expressions() {
        return expressions;
    }

    public Operator operator() {
        return operator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        OperatorExpression that = (OperatorExpression) obj;
        return Objects.equals(this.expressions, that.expressions) &&
               Objects.equals(this.operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions, operator);
    }

    @Override
    public String toString() {
        return "OperatorExpression[" +
               "expressions=" + expressions + ", " +
               "operator=" + operator + ']';
    }

}

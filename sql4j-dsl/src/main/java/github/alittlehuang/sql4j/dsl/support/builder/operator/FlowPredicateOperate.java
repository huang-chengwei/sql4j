package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.OperatorExpression;
import org.jetbrains.annotations.NotNull;

public class FlowPredicateOperate {

    public static Expression and(Expression a, Expression b) {
        if (a == null) {
            return b;
        }
        return a.operate(Operator.AND, b);
    }

    public static Expression or(Expression a, Expression b) {
        return operate(a, Operator.OR, b);
    }

    @NotNull
    public static Expression operate(Expression a, Operator operator, Expression b) {
        if (operator != Operator.AND && operator != Operator.OR) {
            throw new IllegalStateException();
        }
        if (a == null) {
            return b;
        }
        if (operator == Operator.AND && a instanceof OperatorExpression  && ((OperatorExpression) a).operator() == Operator.OR) {
            ConstantArray<Expression> expressions = ConstantArray.from(((OperatorExpression) a).expressions());
            expressions = expressions.replace(expressions.length() - 1, it -> it.operate(Operator.AND, b));
            return Expression.of(expressions, Operator.OR);
        } else {
            return a.operate(operator, b);
        }
    }


}

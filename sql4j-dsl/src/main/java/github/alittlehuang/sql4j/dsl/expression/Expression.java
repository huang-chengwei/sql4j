package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.support.builder.operator.ConstantArray;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.StreamSupport;

public sealed interface Expression extends ExpressionSupplier permits PathExpression, ConstantExpression, OperatorExpression {

    Expression TRUE = new ConstantExpression(true);
    Expression FALSE = new ConstantExpression(false);

    static Expression of(Object value) {
        if (value instanceof ExpressionSupplier builder) {
            return builder.expression();
        }
        return new ConstantExpression(value);
    }

    default Expression operate(Operator operator, Expression expression) {
        return operatorExpression(this, operator, List.of(expression));
    }

    default Expression operate(Operator operator, Object object) {
        return operatorExpression(this, operator, List.of(of(object)));
    }

    default Expression operate(Operator operator, Object o1, Object o2) {
        return operatorExpression(this, operator, List.of(of(o1), of(o2)));
    }

    default Expression operate(Operator operator) {
        return operatorExpression(this, operator, List.of());
    }

    default Expression operate(Operator operator, Iterable<?> objects) {
        List<Expression> expressions = StreamSupport
                .stream(objects.spliterator(), false)
                .map(Expression::of)
                .toList();
        return operatorExpression(this, operator, expressions);
    }

    static Expression of(Collection<? extends Expression> expressions, Operator operator) {
        if ((operator == Operator.AND || operator == Operator.OR) && expressions.size() == 1) {
            return expressions.iterator().next();
        }
        ConstantArray<Expression> array = new ConstantArray<>(expressions);
        return of(array, operator);
    }

    static OperatorExpression of(ConstantArray<Expression> array, Operator operator) {
        return new OperatorExpression(array, operator);
    }

    @NotNull
    private static Expression operatorExpression(Expression expression, Operator operator, Collection<? extends Expression> expressions) {
        if (operator == Operator.AND) {
            if (FALSE.equals(expression) || expressions.stream().anyMatch(FALSE::equals)) {
                return FALSE;
            }
            if (TRUE.equals(expression)) {
                return of(expressions, operator);
            }
            if (expressions.stream().anyMatch(TRUE::equals)) {
                expressions = expressions.stream().filter(it -> !TRUE.equals(it)).toList();
            }
        } else if (operator == Operator.OR) {
            if (TRUE.equals(expression) || expressions.stream().anyMatch(TRUE::equals)) {
                return TRUE;
            }
            if (FALSE.equals(expression)) {
                return of(expressions, operator);
            }
            if (expressions.stream().anyMatch(FALSE::equals)) {
                expressions = expressions.stream().filter(it -> !FALSE.equals(it)).toList();
            }
        }

        List<Expression> list = toList(expression, operator, expressions);
        return of(list, operator);
    }

    @NotNull
    private static List<Expression> toList(Expression expression, Operator operator, Collection<? extends Expression> expressions) {
        if ((operator == Operator.AND || operator == Operator.OR)
            && expression instanceof OperatorExpression oe
            && operator == oe.operator()) {
            ArrayList<Expression> list = new ArrayList<>(expressions.size() + oe.expressions().length());
            for (Expression e : oe.expressions()) {
                list.add(e);
            }
            oe.expressions().forEach(list::add);
            list.addAll(expressions);
            return list;
        } else {
            List<Expression> list = new ArrayList<>(expressions.size() + 1);
            list.add(expression);
            list.addAll(expressions);
            return list;
        }
    }


    @Override
    default Expression expression() {
        return this;
    }
}

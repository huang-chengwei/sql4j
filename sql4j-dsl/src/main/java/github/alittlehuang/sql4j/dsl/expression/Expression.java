package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.support.builder.operator.ConstantArray;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface Expression extends ExpressionSupplier {

    Expression TRUE = new ConstantExpression(true);
    Expression FALSE = new ConstantExpression(false);

    static Expression of(Object value) {
        if (value instanceof ExpressionSupplier) {
            return ((ExpressionSupplier) value).expression();
        }
        return new ConstantExpression(value);
    }

    default Expression operate(Operator operator, Expression expression) {
        return operatorExpression(this, operator, Collections.singletonList(expression));
    }

    default Expression operate(Operator operator, Object object) {
        return operatorExpression(this, operator, Collections.singletonList(of(object)));
    }

    default Expression operate(Operator operator, Object o1, Object o2) {
        return operatorExpression(this, operator, Arrays.asList(of(o1), of(o2)));
    }

    default Expression operate(Operator operator) {
        return operatorExpression(this, operator, Collections.emptyList());
    }

    default Expression operate(Operator operator, Iterable<?> objects) {
        List<Expression> expressions = StreamSupport
                .stream(objects.spliterator(), false)
                .map(Expression::of)
                .collect(Collectors.toList());
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
    static Expression operatorExpression(Expression expression, Operator operator, Collection<? extends Expression> expressions) {
        if (operator == Operator.AND) {
            if (FALSE.equals(expression) || expressions.stream().anyMatch(FALSE::equals)) {
                return FALSE;
            }
            if (TRUE.equals(expression)) {
                return of(expressions, operator);
            }
            if (expressions.stream().anyMatch(TRUE::equals)) {
                expressions = expressions.stream().filter(it -> !TRUE.equals(it)).collect(Collectors.toList());
            }
        } else if (operator == Operator.OR) {
            if (TRUE.equals(expression) || expressions.stream().anyMatch(TRUE::equals)) {
                return TRUE;
            }
            if (FALSE.equals(expression)) {
                return of(expressions, operator);
            }
            if (expressions.stream().anyMatch(FALSE::equals)) {
                expressions = expressions.stream().filter(it -> !FALSE.equals(it)).collect(Collectors.toList());
            }
        }

        List<Expression> list = toList(expression, operator, expressions);
        return of(list, operator);
    }

    @NotNull
    static List<Expression> toList(Expression expression, Operator operator, Collection<? extends Expression> expressions) {
        if ((operator == Operator.AND || operator == Operator.OR)
            && expression instanceof OperatorExpression
            && operator == ((OperatorExpression) expression).operator()) {
            OperatorExpression oe = (OperatorExpression) expression;
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

package github.sql4j.dsl.expression;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface Expression<T> {

    static Expression<?> of(Object value) {
        if (value instanceof Expression) {
            return (Expression<?>) value;
        }
        return new ConstantExpression<>(value);
    }

    default <X> Expression<X> then(Operator operator, Object... args) {
        List<? extends Expression<?>> list = Arrays.stream(args)
                .map(Expression::of)
                .collect(Collectors.toList());
        return thenOperator(this, operator, list);
    }

    default <X> Expression<X> then(Operator operator, @NotNull Collection<?> args) {
        List<? extends Expression<?>> list = args.stream().map(Expression::of)
                .collect(Collectors.toList());
        return thenOperator(this, operator, list);
    }

    static <T, X> OperatorExpressionImpl<X> thenOperator(Expression<T> e, Operator operator, Expression<?>... args) {
        if (args == null || args.length == 0) {
            return new OperatorExpressionImpl<>(Collections.singletonList(e), operator);
        }
        Expression<?>[] expressions = new Expression[args.length + 1];
        expressions[0] = e;
        System.arraycopy(args, 0, expressions, 1, args.length);
        return new OperatorExpressionImpl<>(Arrays.asList(expressions), operator);
    }

    static <T, X> OperatorExpressionImpl<X> thenOperator(Expression<T> e, Operator operator, Collection<? extends Expression<?>> args) {
        return thenOperator(e, operator, args == null ? new Expression[0] : args.toArray(new Expression[0]));
    }

    PathExpression<T> asPathExpression();

    Type getType();

    T getValue();

    Operator getOperator();

    List<? extends Expression<?>> getExpressions();

    enum Type {
        PATH,
        CONSTANT,
        OPERATOR,
    }

}

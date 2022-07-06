package github.sql4j.dsl.expression;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface SqlExpression<T> {

    static SqlExpression<?> of(Object value) {
        if (value instanceof SqlExpression) {
            return (SqlExpression<?>) value;
        }
        return new ConstantExpression<>(value);
    }

    default <X> SqlExpression<X> then(Operator operator, Object... args) {
        List<? extends SqlExpression<?>> list = Arrays.stream(args)
                .map(SqlExpression::of)
                .collect(Collectors.toList());
        return thenOperator(this, operator, list);
    }

    default <X> SqlExpression<X> then(Operator operator, @NotNull Collection<?> args) {
        List<? extends SqlExpression<?>> list = args.stream().map(SqlExpression::of)
                .collect(Collectors.toList());
        return thenOperator(this, operator, list);
    }

    static <T, X> OperatorExpressionImpl<X> thenOperator(SqlExpression<T> e, Operator operator, SqlExpression<?>... args) {
        if (args == null || args.length == 0) {
            return new OperatorExpressionImpl<>(Collections.singletonList(e), operator);
        }
        SqlExpression<?>[] expressions = new SqlExpression[args.length + 1];
        expressions[0] = e;
        System.arraycopy(args, 0, expressions, 1, args.length);
        return new OperatorExpressionImpl<>(Arrays.asList(expressions), operator);
    }

    static <T, X> OperatorExpressionImpl<X> thenOperator(SqlExpression<T> e, Operator operator, Collection<? extends SqlExpression<?>> args) {
        return thenOperator(e, operator, args == null ? new SqlExpression[0] : args.toArray(new SqlExpression[0]));
    }

    PathExpression<T> asPathExpression();

    Type getType();

    T getValue();

    Operator getOperator();

    List<? extends SqlExpression<?>> getExpressions();

    enum Type {
        PATH,
        CONSTANT,
        OPERATOR,
    }

}

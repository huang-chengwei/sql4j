package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.PredicateOperator;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class AbstractPredicateOperator<T, U, BUILDER> implements PredicateOperator<T, U, BUILDER> {

    protected final DataAction<? extends Expression, ? super Expression, BUILDER> dataBuilder;

    public AbstractPredicateOperator(DataAction<? extends Expression, ? super Expression, BUILDER> dataBuilder) {
        this.dataBuilder = dataBuilder;
    }

    protected BUILDER build(Operator operator, Object value) {
        if (operator == Operator.EQ && Boolean.TRUE.equals(value)) {
            return dataBuilder.builder().apply(dataBuilder.data());
        }
        Expression update = dataBuilder.data().operate(operator, value);
        return dataBuilder.builder().apply(update);
    }

    protected BUILDER build(Operator operator, Object v1, Object v2) {
        Expression update = dataBuilder.data().operate(operator, v1, v2);
        return dataBuilder.builder().apply(update);
    }

    protected BUILDER build(Operator operator, Iterable<?> values) {
        Expression update = dataBuilder.data().operate(operator, values);
        return dataBuilder.builder().apply(update);
    }

    protected BUILDER build(Operator operator, ColumnGetter<?, ?> attribute) {
        Expression update = dataBuilder.data().operate(operator, (Expression) attribute.expression());
        return dataBuilder.builder().apply(update);
    }

    protected BUILDER build(Operator operator) {
        Expression update = dataBuilder.data().operate(operator);
        return dataBuilder.builder().apply(update);
    }


    protected DataAction<? extends Expression, ? super Expression, BUILDER> operate(Operator operator, Object value) {
        Expression update = dataBuilder.data().operate(operator, value);
        return new DataAction<>(update, dataBuilder.builder());
    }

    protected DataAction<? extends Expression, ? super Expression, BUILDER> operate(Operator operator, Object v1, Object v2) {
        Expression update = dataBuilder.data().operate(operator, v1, v2);
        return new DataAction<>(update, dataBuilder.builder());
    }

    protected DataAction<? extends Expression, ? super Expression, BUILDER> operate(Operator operator, Iterable<?> values) {
        Expression update = dataBuilder.data().operate(operator, values);
        return new DataAction<>(update, dataBuilder.builder());
    }

    protected DataAction<? extends Expression, ? super Expression, BUILDER> operate(Operator operator, ColumnGetter<?, ?> attribute) {
        Expression update = dataBuilder.data().operate(operator, (Expression) attribute.expression());
        return new DataAction<>(update, dataBuilder.builder());
    }

    protected DataAction<? extends Expression, ? super Expression, BUILDER> operate(Operator operator) {
        Expression update = dataBuilder.data().operate(operator);
        return new DataAction<>(update, dataBuilder.builder());
    }


    public BUILDER isNull() {
        return build(Operator.ISNULL);
    }

    public BUILDER eq(U value) {
        return build(Operator.EQ, value);
    }

    public BUILDER ne(U value) {
        return build(Operator.NE, value);
    }

    public BUILDER in(Iterable<? extends U> values) {
        return build(Operator.IN, values);
    }

    public BUILDER ge(U value) {
        return build(Operator.GE, value);
    }

    public BUILDER gt(U value) {
        return build(Operator.GT, value);
    }

    public BUILDER le(U value) {
        return build(Operator.LE, value);
    }

    public BUILDER between(U a, U b) {
        return build(Operator.BETWEEN, a, b);
    }

    public BUILDER lt(U value) {
        return build(Operator.LT, value);
    }

    public PredicateOperator<T, U, BUILDER> nullIf(U value) {
        var builder = dataBuilder.data(data -> data.operate(Operator.NULLIF, value));
        return new DefaultPredicateOperator<>(builder);
    }

    public PredicateOperator<T, U, BUILDER> ifNull(U value) {
        var builder = dataBuilder.data(data -> data.operate(Operator.IF_NULL, value));
        return new DefaultPredicateOperator<>(builder);
    }

    @NotNull
    protected DataAction<? extends Expression, ? super Expression, BUILDER> negateDataBuilder() {
        Function<? super Expression, BUILDER> update = expression -> dataBuilder.builder().apply(expression.operate(Operator.NOT));
        return new DataAction<>(dataBuilder.data(), update);
    }


}

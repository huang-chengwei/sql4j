package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.NumberOperator;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.path.NumberGetter;

public class DefaultNumberOperator<T, U extends Number & Comparable<?>, BUILDER>
        extends DefaultComparableOperator<T, U, BUILDER>
        implements NumberOperator<T, U, BUILDER> {

    public DefaultNumberOperator(DataAction<? extends Expression, ? super Expression, BUILDER> dataBuilder) {
        super(dataBuilder);
    }

    @Override
    public NumberOperator<T, U, BUILDER> add(U v) {
        return new DefaultNumberOperator<>(operate(Operator.ADD, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> subtract(U v) {
        return new DefaultNumberOperator<>(operate(Operator.SUBTRACT, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> multiply(U v) {
        return new DefaultNumberOperator<>(operate(Operator.MULTIPLY, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> divide(U v) {
        return new DefaultNumberOperator<>(operate(Operator.DIVIDE, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> mod(U v) {
        return new DefaultNumberOperator<>(operate(Operator.MOD, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> add(NumberGetter<T, U> v) {
        return new DefaultNumberOperator<>(operate(Operator.ADD, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> subtract(NumberGetter<T, U> v) {
        return new DefaultNumberOperator<>(operate(Operator.SUBTRACT, v));
    }

    @Override
    public NumberOperator<T, U, BUILDER> multiply(NumberGetter<T, U> v) {
        return new DefaultNumberOperator<>(operate(Operator.MULTIPLY, v));

    }

    @Override
    public NumberOperator<T, U, BUILDER> divide(NumberGetter<T, U> v) {
        return new DefaultNumberOperator<>(operate(Operator.DIVIDE, v));

    }

    @Override
    public NumberOperator<T, U, BUILDER> mod(NumberGetter<T, U> v) {
        return new DefaultNumberOperator<>(operate(Operator.MOD, v));
    }
}

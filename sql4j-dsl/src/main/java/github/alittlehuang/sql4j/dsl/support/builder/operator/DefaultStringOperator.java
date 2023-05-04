package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.NumberOperator;
import github.alittlehuang.sql4j.dsl.builder.StringOperator;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;

public class DefaultStringOperator<T, BUILDER> extends DefaultComparableOperator<T, String, BUILDER> implements StringOperator<T, BUILDER> {


    public DefaultStringOperator(DataAction<? extends Expression, ? super Expression, BUILDER> dataBuilder) {
        super(dataBuilder);
    }


    @Override
    public BUILDER like(String value) {
        return build(Operator.LIKE, value);
    }

    @Override
    public StringOperator<T, BUILDER> lower() {
        return new DefaultStringOperator<>(operate(Operator.LOWER));
    }

    @Override
    public StringOperator<T, BUILDER> upper() {
        return new DefaultStringOperator<>(operate(Operator.UPPER));
    }

    @Override
    public StringOperator<T, BUILDER> substring(int a, int b) {
        return new DefaultStringOperator<>(operate(Operator.SUBSTRING, a, b));
    }

    @Override
    public StringOperator<T, BUILDER> substring(int a) {
        return new DefaultStringOperator<>(operate(Operator.SUBSTRING, a));
    }

    @Override
    public StringOperator<T, BUILDER> trim() {
        return new DefaultStringOperator<>(operate(Operator.TRIM));
    }

    @Override
    public NumberOperator<T, Integer, BUILDER> length() {
        return new DefaultNumberOperator<>(operate(Operator.LENGTH));
    }
}

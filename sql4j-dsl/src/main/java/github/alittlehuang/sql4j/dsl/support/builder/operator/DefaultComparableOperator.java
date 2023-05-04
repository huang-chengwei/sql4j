package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.ComparableOperator;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.path.ComparableGetter;

public class DefaultComparableOperator<T, U extends Comparable<?>, BUILDER>
        extends AbstractPredicateOperator<T, U, BUILDER>
        implements ComparableOperator<T, U, BUILDER> {


    public DefaultComparableOperator(DataAction<? extends Expression, ? super Expression, BUILDER> dataBuilder) {
        super(dataBuilder);
    }

    @Override
    public BUILDER ge(ComparableGetter<T, U> value) {
        return build(Operator.GE, value);
    }

    @Override
    public BUILDER gt(ComparableGetter<T, U> value) {
        return build(Operator.GT, value);
    }

    @Override
    public BUILDER le(ComparableGetter<T, U> value) {
        return build(Operator.LE, value);
    }

    @Override
    public BUILDER between(ComparableGetter<T, U> a, ComparableGetter<T, U> b) {
        return build(Operator.BETWEEN, a, b);
    }

    @Override
    public BUILDER lt(ComparableGetter<T, U> value) {
        return build(Operator.LT, value);
    }

    @Override
    public ComparableOperator<T, U, BUILDER> not() {
        return new DefaultComparableOperator<>(negateDataBuilder());
    }

}

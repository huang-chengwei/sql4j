package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.*;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.Predicate;
import github.alittlehuang.sql4j.dsl.expression.path.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DefaultWhere<T, BUILDER> implements Where<T, BUILDER> {

    private final Function<Expression, BUILDER> dataBuilder;

    public DefaultWhere(Function<Expression, BUILDER> dataBuilder) {
        this.dataBuilder = dataBuilder;
    }


    @Override
    public <U extends Persistable> PathBuilder<T, U, BUILDER> where(EntityGetter<T, U> reference) {
        DataAction<PathExpression, Expression, BUILDER> dataBuilder = newDataBuilder(reference);
        return new DefaultPathBuilder<>(dataBuilder);
    }

    @NotNull
    private DataAction<PathExpression, Expression, BUILDER> newDataBuilder(ColumnGetter<?, ?> column) {
        return new DataAction<>(column.expression(), this::build);
    }

    private BUILDER build(Expression expression) {
        return dataBuilder.apply(expression);
    }


    @Override
    public <U> PredicateOperator<T, U, BUILDER> where(ColumnGetter<T, U> reference) {
        return new DefaultPredicateOperator<>(newDataBuilder(reference));
    }

    @Override
    public <U extends Number & Comparable<?>> NumberOperator<T, U, BUILDER> where(NumberGetter<T, U> reference) {
        return new DefaultNumberOperator<>(newDataBuilder(reference));
    }

    @Override
    public <U extends Comparable<?>> ComparableOperator<T, U, BUILDER> where(ComparableGetter<T, U> reference) {
        return new DefaultComparableOperator<>(newDataBuilder(reference));
    }

    @Override
    public StringOperator<T, BUILDER> where(StringGetter<T> reference) {
        return new DefaultStringOperator<>(newDataBuilder(reference));

    }

    @Override
    public BUILDER where(Predicate<T> predicate) {
        return build(predicate.expression());
    }

    @Override
    public Where<T, BUILDER> not() {
        return new DefaultWhere<>(expression -> dataBuilder.apply(expression.operate(Operator.NOT)));
    }


}

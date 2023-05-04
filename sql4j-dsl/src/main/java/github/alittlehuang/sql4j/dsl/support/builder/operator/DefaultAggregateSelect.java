package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.AggregateSelect;
import github.alittlehuang.sql4j.dsl.expression.AggregateFunction;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DefaultAggregateSelect<T, B> implements AggregateSelect<T, B> {
    private final Function<Expression, B> mapper;

    public DefaultAggregateSelect(Function<Expression, B> mapper) {
        this.mapper = mapper;
    }

    @Override
    public B select(ColumnGetter<T, ?> attribute, @NotNull AggregateFunction function) {
        Expression expression = attribute.expression().operate(function.getOperator());
        return mapper.apply(expression);
    }
}

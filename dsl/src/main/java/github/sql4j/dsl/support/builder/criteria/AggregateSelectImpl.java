package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.AggregateSelect;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.support.builder.component.AggregateFunction;
import github.sql4j.dsl.support.builder.component.ConstantArray;
import github.sql4j.dsl.util.Array;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AggregateSelectImpl<T, BUILDER> implements AggregateSelect<T, BUILDER> {

    private final Array<Expression<?>> values;
    private final Function<Array<Expression<?>>, BUILDER> mapper;

    public AggregateSelectImpl(Array<Expression<?>> values,
                               Function<Array<Expression<?>>, BUILDER> mapper) {
        this.values = values;
        this.mapper = mapper;
    }

    public BUILDER select(Attribute<T, ?> attribute, @NotNull AggregateFunction function) {
        Expression<?> path = AttributePath.exchange(attribute);
        Expression<?> s = path.then(function.getOperator());
        Array<Expression<?>> list = values == null ? new ConstantArray<>(s) : ConstantArray.from(values).concat(s);
        return mapper.apply(list);
    }

}

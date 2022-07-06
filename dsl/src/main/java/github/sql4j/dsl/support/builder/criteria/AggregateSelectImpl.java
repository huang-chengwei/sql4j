package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.AggregateSelect;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.support.builder.component.AggregateFunction;
import github.sql4j.dsl.support.builder.component.ConstantArray;
import github.sql4j.dsl.util.Array;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AggregateSelectImpl<T, BUILDER> implements AggregateSelect<T, BUILDER> {

    private final Array<SqlExpression<?>> values;
    private final Function<Array<SqlExpression<?>>, BUILDER> mapper;

    public AggregateSelectImpl(Array<SqlExpression<?>> values,
                               Function<Array<SqlExpression<?>>, BUILDER> mapper) {
        this.values = values;
        this.mapper = mapper;
    }

    public BUILDER select(Attribute<T, ?> attribute, @NotNull AggregateFunction function) {
        SqlExpression<?> path = AttributePath.exchange(attribute);
        SqlExpression<?> s = path.then(function.getOperator());
        Array<SqlExpression<?>> list = values == null ? new ConstantArray<>(s) : ConstantArray.from(values).concat(s);
        return mapper.apply(list);
    }

}

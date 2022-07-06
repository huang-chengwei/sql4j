package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.GroupBy;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.support.builder.component.ConstantArray;
import github.sql4j.dsl.util.Array;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GroupByImpl<T, BUILDER> implements GroupBy<T, BUILDER> {

    private Array<SqlExpression<?>> values;
    private final Function<Array<SqlExpression<?>>, BUILDER> mapper;

    public GroupByImpl(Array<SqlExpression<?>> values, Function<Array<SqlExpression<?>>, BUILDER> mapper) {
        this.values = values == null ? new ConstantArray<>() : values;
        this.mapper = mapper;
    }

    @Override
    public BUILDER groupBy(Attribute<T, ?> attribute) {
        AttributePath<T, ?> path = AttributePath.exchange(attribute);
        values = ConstantArray.from(values).concat(path);
        return mapper.apply(values);
    }

    @Override
    public BUILDER groupBy(List<Attribute<T, ?>> attributes) {
        List<? extends AttributePath<T, ?>> list = attributes.stream().map(AttributePath::exchange)
                .collect(Collectors.toList());
        values = ConstantArray.from(values).concat(list);
        return mapper.apply(values);
    }

}

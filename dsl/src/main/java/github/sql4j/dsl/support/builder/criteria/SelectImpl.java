package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.Select;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.support.builder.component.ConstantArray;
import github.sql4j.dsl.util.Array;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SelectImpl<T, BUILDER> implements Select<T, BUILDER> {

    private final Array<Expression<?>> values;
    private final Function<Array<Expression<?>>, BUILDER> mapper;

    public SelectImpl(Array<Expression<?>> values,
                      Function<Array<Expression<?>>, BUILDER> mapper) {
        this.values = values;
        this.mapper = mapper;
    }

    @Override
    public BUILDER select(Attribute<T, ?> selection) {
        AttributePath<T, ?> path = AttributePath.exchange(selection);
        Array<Expression<?>> list = values == null ? new ConstantArray<>(path) : ConstantArray.from(values).concat(path);
        return mapper.apply(list);
    }

    @Override
    public BUILDER select(List<Attribute<T, ?>> selections) {
        List<? extends AttributePath<T, ?>> paths = selections.stream()
                .map(AttributePath::exchange)
                .collect(Collectors.toList());
        Array<Expression<?>> list = values == null
                ? new ConstantArray<>(paths)
                : ConstantArray.from(values).concat(paths);
        return mapper.apply(list);
    }
}

package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.GroupBy;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultGroupBy<T, B> implements GroupBy<T, B> {
    private final Function<List<Expression>, B> mapper;

    public DefaultGroupBy(Function<List<Expression>, B> mapper) {
        this.mapper = mapper;
    }

    @Override
    public B groupBy(ColumnGetter<T, ?> attribute) {
        return groupBy(Collections.singletonList(attribute));
    }

    @Override
    public B groupBy(List<ColumnGetter<T, ?>> attributes) {
        List<Expression> list = attributes.stream()
                .<Expression>map(ColumnGetter::expression)
                .collect(Collectors.toList());
        return mapper.apply(list);
    }
}

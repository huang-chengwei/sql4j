package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.GroupBy;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;

import java.util.List;
import java.util.function.Function;

public class DefaultGroupBy<T, B> implements GroupBy<T, B> {
    private final Function<List<Expression>, B> mapper;

    public DefaultGroupBy(Function<List<Expression>, B> mapper) {
        this.mapper = mapper;
    }

    @Override
    public B groupBy(ColumnGetter<T, ?> attribute) {
        return groupBy(List.of(attribute));
    }

    @Override
    public B groupBy(List<ColumnGetter<T, ?>> attributes) {
        List<Expression> list = attributes.stream()
                .<Expression>map(ColumnGetter::expression)
                .toList();
        return mapper.apply(list);
    }
}

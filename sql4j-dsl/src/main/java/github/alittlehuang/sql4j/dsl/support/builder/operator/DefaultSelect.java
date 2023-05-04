package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.Select;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;

import java.util.List;
import java.util.function.Function;

public class DefaultSelect<T, B> implements Select<T, B> {
    private final Function<List<Expression>, B> mapper;

    public DefaultSelect(Function<List<Expression>, B> mapper) {
        this.mapper = mapper;
    }

    @Override
    public B select(ColumnGetter<T, ?> selection) {
        return select(List.of(selection));
    }

    @Override
    public B select(List<ColumnGetter<T, ?>> selections) {
        List<Expression> list = selections.stream()
                .<Expression>map(ColumnGetter::expression)
                .toList();
        return mapper.apply(list);
    }
}

package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.Select;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultSelect<T, B> implements Select<T, B> {
    private final Function<List<Expression>, B> mapper;

    public DefaultSelect(Function<List<Expression>, B> mapper) {
        this.mapper = mapper;
    }

    @Override
    public B select(ColumnGetter<T, ?> selection) {
        return select(Collections.singletonList(selection));
    }

    @Override
    public B select(List<ColumnGetter<T, ?>> selections) {
        List<Expression> list = selections.stream()
                .<Expression>map(ColumnGetter::expression)
                .collect(Collectors.toList());
        return mapper.apply(list);
    }
}

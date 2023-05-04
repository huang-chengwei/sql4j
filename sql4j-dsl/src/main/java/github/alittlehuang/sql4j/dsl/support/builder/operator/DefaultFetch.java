package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.Fetch;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.path.EntityGetter;

import java.util.function.Function;

public class DefaultFetch<T, B> implements Fetch<T, B> {

    private final Function<PathExpression, B> builder;

    public DefaultFetch(Function<PathExpression, B> builder) {
        this.builder = builder;
    }

    @Override
    public B fetch(EntityGetter<T, ?> column) {
        return builder.apply(column.expression());
    }

}

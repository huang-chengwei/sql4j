package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.AggregateFunction;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;
import org.jetbrains.annotations.NotNull;

public interface AggregateSelect<T, BUILDER> {

    BUILDER select(ColumnGetter<T, ?> attribute, @NotNull AggregateFunction function);

}

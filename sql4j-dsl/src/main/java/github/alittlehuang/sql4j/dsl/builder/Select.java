package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;

import java.util.List;

public interface Select<T, BUILDER> {

    BUILDER select(ColumnGetter<T, ?> selection);

    BUILDER select(List<ColumnGetter<T, ?>> selections);

}

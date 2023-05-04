package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;

import java.util.List;

public interface GroupBy<T, BUILDER> {

    BUILDER groupBy(ColumnGetter<T, ?> attribute);

    BUILDER groupBy(List<ColumnGetter<T, ?>> attributes);

}

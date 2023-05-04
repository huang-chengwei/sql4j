package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.EntityGetter;

public interface Fetch<T, BUILDER> {

    BUILDER fetch(EntityGetter<T, ?> column);

}

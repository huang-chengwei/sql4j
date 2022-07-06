package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.path.attribute.EntityAttribute;

public interface Fetch<T, BUILDER> {

    BUILDER fetch(EntityAttribute<T, ?> column);

}

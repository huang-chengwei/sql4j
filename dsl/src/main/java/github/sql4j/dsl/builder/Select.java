package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.path.attribute.Attribute;

import java.util.List;

public interface Select<T, BUILDER> {

    BUILDER select(Attribute<T, ?> selection);

    BUILDER select(List<Attribute<T, ?>> selections);

}

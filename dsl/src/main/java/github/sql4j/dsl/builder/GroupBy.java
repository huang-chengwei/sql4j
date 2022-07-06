package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.path.attribute.Attribute;

import java.util.List;

public interface GroupBy<T, BUILDER> {

    BUILDER groupBy(Attribute<T, ?> attribute);

    BUILDER groupBy(List<Attribute<T, ?>> attributes);

}

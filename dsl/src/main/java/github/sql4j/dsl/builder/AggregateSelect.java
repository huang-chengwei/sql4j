package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.path.attribute.Attribute;
import github.sql4j.dsl.support.builder.component.AggregateFunction;
import org.jetbrains.annotations.NotNull;

public interface AggregateSelect<T, BUILDER> {

    BUILDER select(Attribute<T, ?> attribute, @NotNull AggregateFunction function);

}

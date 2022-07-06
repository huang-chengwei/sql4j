package github.sql4j.dsl.support.builder.criteria;

import github.sql4j.dsl.builder.Fetch;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.EntityPath;
import github.sql4j.dsl.expression.path.attribute.EntityAttribute;
import github.sql4j.dsl.support.builder.component.ConstantArray;
import github.sql4j.dsl.util.Array;
import lombok.Getter;

import java.util.function.Function;

@Getter
public class FetchImpl<T, BUILDER> implements Fetch<T, BUILDER> {

    private final Array<PathExpression<?>> values;
    private final Function<Array<PathExpression<?>>, BUILDER> mapper;

    public FetchImpl(Array<PathExpression<?>> values,
                     Function<Array<PathExpression<?>>, BUILDER> mapper) {
        this.values = values;
        this.mapper = mapper;
    }

    @Override
    public BUILDER fetch(EntityAttribute<T, ?> attribute) {
        EntityPath<T, ?> exchange = AttributePath.exchange(attribute);
        Array<PathExpression<?>> then = this.values == null
                ? new ConstantArray<>(exchange)
                : ConstantArray.from(this.values).concat(exchange);
        return mapper.apply(then);
    }

}

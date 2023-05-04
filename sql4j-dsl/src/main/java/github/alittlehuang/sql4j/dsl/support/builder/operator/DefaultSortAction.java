package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.SortAction;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.SortSpecification;
import org.jetbrains.annotations.NotNull;

public class DefaultSortAction<B> implements SortAction<B> {
    private final DataAction<PathExpression, SortSpecification, B> dataBuilder;

    public DefaultSortAction(@NotNull DataAction<PathExpression, SortSpecification, B> dataBuilder) {
        this.dataBuilder = dataBuilder;
    }

    @Override
    public B asc() {
        return build(false);
    }

    @Override
    public B desc() {
        return build(true);
    }

    private B build(boolean desc) {
        SortSpecification order = new SortSpecification(dataBuilder.data(), desc);
        return dataBuilder.builder().apply(order);
    }
}

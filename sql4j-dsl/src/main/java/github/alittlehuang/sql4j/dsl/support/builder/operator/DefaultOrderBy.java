package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.OrderBy;
import github.alittlehuang.sql4j.dsl.builder.SortAction;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.SortSpecification;
import github.alittlehuang.sql4j.dsl.expression.path.ColumnGetter;
import github.alittlehuang.sql4j.dsl.expression.path.ComparableGetter;
import github.alittlehuang.sql4j.dsl.expression.path.NumberGetter;
import github.alittlehuang.sql4j.dsl.expression.path.StringGetter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DefaultOrderBy<T, B> implements OrderBy<T, B> {

    private final Function<SortSpecification, B> builder;

    public DefaultOrderBy(Function<SortSpecification, B> builder) {
        this.builder = builder;
    }

    @Override
    public <U extends Number & Comparable<?>> SortAction<B> orderBy(NumberGetter<T, U> column) {
        return new DefaultSortAction<>(getDataBuilder(column));
    }

    @Override
    public <U extends Comparable<?>> SortAction<B> orderBy(ComparableGetter<T, U> column) {
        return new DefaultSortAction<>(getDataBuilder(column));
    }

    @Override
    public SortAction<B> orderBy(StringGetter<T> column) {
        return new DefaultSortAction<>(getDataBuilder(column));
    }

    @NotNull
    private <U> DataAction<PathExpression, SortSpecification, B> getDataBuilder(ColumnGetter<T, U> column) {
        return new DataAction<>(column.expression(), builder);
    }
}

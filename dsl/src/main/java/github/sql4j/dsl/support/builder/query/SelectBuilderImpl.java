package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.builder.Select;
import github.sql4j.dsl.builder.Sort;
import github.sql4j.dsl.builder.SelectBuilder;
import github.sql4j.dsl.builder.Where;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import github.sql4j.dsl.util.Tuple;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class SelectBuilderImpl<T> extends AbstractResult<T> implements SelectBuilder<T> {

    public SelectBuilderImpl(TypeQueryFactory typeQueryFactory, Class<T> entityType, StructuredQuery criteriaQuery) {
        super(typeQueryFactory, entityType, criteriaQuery);
    }

    @Delegate
    @Override
    protected @NotNull Where<T, SelectBuilder<T>> getObjectsWhere() {
        return super.getObjectsWhere();
    }

    @Delegate
    @Override
    protected @NotNull Sort<T, SelectBuilder<T>> getObjectsSortable() {
        return super.getObjectsSortable();
    }

    @Delegate
    @Override
    protected @NotNull Select<T, SelectBuilder<T>> getSelectable() {
        return super.getSelectable();
    }

    @Delegate
    @Override
    protected ResultBuilder<Tuple> getObjectsTypeQuery() {
        return super.getObjectsTypeQuery();
    }

}
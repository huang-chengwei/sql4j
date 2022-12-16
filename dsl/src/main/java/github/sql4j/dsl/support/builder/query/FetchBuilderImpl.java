package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.Fetch;
import github.sql4j.dsl.builder.PredicateAssembler;
import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.builder.Sort;
import github.sql4j.dsl.builder.FetchBuilder;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class FetchBuilderImpl<T> extends AbstractResult<T> implements FetchBuilder<T> {

    public FetchBuilderImpl(TypeQueryFactory typeQueryFactory, Class<T> entityType, StructuredQuery criteriaQuery) {
        super(typeQueryFactory, entityType, criteriaQuery);
    }

    @Delegate
    @Override
    protected @NotNull PredicateAssembler<T, FetchBuilder<T>> getRestrictionBuilder() {
        return super.getRestrictionBuilder();
    }

    @Delegate
    @Override
    protected @NotNull Fetch<T, FetchBuilder<T>> getFetch() {
        return super.getFetch();
    }

    @Delegate
    protected @NotNull Sort<T, FetchBuilder<T>> getEntityQuerySortable() {
        return super.getEntityQuerySortable();
    }


    @Delegate
    @Override
    protected ResultBuilder<T> getTypeQuery() {
        return super.getTypeQuery();
    }
}

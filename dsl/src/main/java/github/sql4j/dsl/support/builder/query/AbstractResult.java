package github.sql4j.dsl.support.builder.query;

import github.sql4j.dsl.builder.*;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;
import github.sql4j.dsl.support.builder.criteria.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractResult<T> {


    protected final TypeQueryFactory typeQueryFactory;
    protected final Class<T> entityType;
    protected final CriteriaQueryImpl criteriaQuery;

    public AbstractResult(TypeQueryFactory typeQueryFactory, Class<T> entityType, StructuredQuery criteriaQuery) {
        this.typeQueryFactory = typeQueryFactory;
        this.entityType = entityType;
        this.criteriaQuery = CriteriaQueryImpl.from(criteriaQuery);
    }

    protected ResultBuilder<Object[]> getObjectsTypeQuery() {
        return typeQueryFactory.getObjectsTypeQuery(criteriaQuery, entityType);
    }

    protected ResultBuilder<T> getTypeQuery() {
        return typeQueryFactory.getEntityResultQuery(criteriaQuery, entityType);
    }

    @NotNull
    protected Select<T, SelectBuilder<T>> getSelectable() {
        return new SelectImpl<>(this.criteriaQuery.select(), next ->
                new SelectBuilderImpl<>(this.typeQueryFactory, this.entityType, this.criteriaQuery.updateSelection(next)));
    }

    @NotNull
    protected GroupBy<T, GroupByBuilder<T>> getGroupBy() {
        return new GroupByImpl<>(criteriaQuery.groupBy(), next -> new GroupByBuilderImpl<>(
                this.typeQueryFactory,
                this.entityType,
                this.criteriaQuery.updateGroupList(next)));
    }

    @NotNull
    protected Sort<T, WhereBuilder<T>> getSortable() {
        return new SortImpl<>(criteriaQuery.orderBy(),
                next -> whereBuilder(this.criteriaQuery.updateOrderList(next)));
    }

    @NotNull
    protected Fetch<T, FetchBuilder<T>> getFetch() {
        return new FetchImpl<>(criteriaQuery.fetch(),
                next -> new FetchBuilderImpl<>(typeQueryFactory, entityType, criteriaQuery.updateFetch(next)));
    }

    @NotNull
    protected WhereImpl<T, WhereBuilder<T>> getWhere() {
        return new WhereImpl<>(next -> whereBuilder(criteriaQuery.updateRestriction(next)));
    }

    @NotNull
    protected PredicateCombiner<T, FetchBuilder<T>> getRestrictionBuilder() {
        return new PredicateCombinerImpl<>(criteriaQuery.where(),
                next -> new FetchBuilderImpl<>(typeQueryFactory, entityType, criteriaQuery.updateRestriction(next)));
    }


    protected WhereBuilder<T> whereBuilder(StructuredQuery criteriaQuery) {
        return new WhereBuilderImpl<>(this.typeQueryFactory, this.entityType, criteriaQuery);
    }

    @NotNull
    protected Where<T, SelectBuilder<T>> getObjectsWhere() {
        return new WhereImpl<>(next -> {
            CriteriaQueryImpl updated = this.criteriaQuery.updateRestriction(next);
            return new SelectBuilderImpl<>(typeQueryFactory, entityType, updated);
        });
    }

    @NotNull
    protected Sort<T, SelectBuilder<T>> getObjectsSortable() {
        return new SortImpl<>(criteriaQuery.orderBy(), next -> {
            CriteriaQueryImpl updated = this.criteriaQuery.updateOrderList(next);
            return new SelectBuilderImpl<>(typeQueryFactory, entityType, updated);
        });
    }

    protected @NotNull PredicateCombiner<T, WhereBuilder<T>> getWereBuilderRestrictionBuilder() {
        return new PredicateCombinerImpl<>(criteriaQuery.where(), next -> {
            CriteriaQueryImpl updated = this.criteriaQuery.updateRestriction(next);
            return new WhereBuilderImpl<>(typeQueryFactory, entityType, updated);
        });
    }

    protected @NotNull Sort<T, FetchBuilder<T>> getEntityQuerySortable() {
        return new SortImpl<>(criteriaQuery.orderBy(), next -> {
            CriteriaQueryImpl updated = this.criteriaQuery.updateOrderList(next);
            return new FetchBuilderImpl<>(typeQueryFactory, entityType, updated);
        });
    }

    protected @NotNull AggregateSelect<T, AggregateSelectBuilder<T>> getAggregateSelectable() {
        return new AggregateSelectImpl<>(this.criteriaQuery.select(), next ->
                new AggregateSelectBuilderImpl<>(this.typeQueryFactory, this.entityType, this.criteriaQuery.updateSelection(next)));
    }

    public <R> ResultBuilder<R> projected(Class<R> projectionType) {
        return typeQueryFactory.getProjectionQuery(this.criteriaQuery, entityType, projectionType);
    }

}

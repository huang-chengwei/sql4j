package github.sql4j.dsl.builder;

public interface WhereBuilder<T> extends
        PredicateCombiner<T, WhereBuilder<T>>,
        Fetch<T, FetchBuilder<T>>,
        Sort<T, WhereBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        Projection<T>,
        ResultBuilder<T> {


}

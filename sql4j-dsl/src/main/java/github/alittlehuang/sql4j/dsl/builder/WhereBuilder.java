package github.alittlehuang.sql4j.dsl.builder;

public interface WhereBuilder<T> extends
        PredicateCombiner<T, WhereBuilder<T>>,
        Fetch<T, FetchBuilder<T>>,
        OrderBy<T, WhereBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        Projection,
        ResultBuilder<T>,
        Mapper<WhereBuilder<T>> {


}

package github.alittlehuang.sql4j.dsl.builder;

public interface Query<T> extends
        Where<T, WhereBuilder<T>>,
        Fetch<T, FetchBuilder<T>>,
        OrderBy<T, WhereBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        Projection,
        ResultBuilder<T>,
        Mapper<Query<T>> {


}

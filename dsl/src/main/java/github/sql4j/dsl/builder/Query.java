package github.sql4j.dsl.builder;

public interface Query<T> extends
        Where<T, WhereBuilder<T>>,
        Fetch<T, FetchBuilder<T>>,
        Sort<T, WhereBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        Projection<T>,
        ResultBuilder<T> {


}

package github.sql4j.dsl.builder;

public interface GroupByBuilder<T> extends
        GroupBy<T, GroupByBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>> {


}

package github.sql4j.dsl.builder;

public interface AggregateSelectBuilder<T> extends
        Where<T, SelectBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        ResultBuilder<Object[]> {


}

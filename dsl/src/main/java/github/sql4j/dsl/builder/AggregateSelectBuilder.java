package github.sql4j.dsl.builder;


import github.sql4j.dsl.util.Tuple;

public interface AggregateSelectBuilder<T> extends
        Where<T, SelectBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        ResultBuilder<Tuple> {


}

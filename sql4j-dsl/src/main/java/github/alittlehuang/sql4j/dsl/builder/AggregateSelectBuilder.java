package github.alittlehuang.sql4j.dsl.builder;


import github.alittlehuang.sql4j.dsl.util.Tuple;

public interface AggregateSelectBuilder<T> extends
        Where<T, SelectBuilder<T>>,
        GroupBy<T, GroupByBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        ResultBuilder<Tuple>,
        Mapper<AggregateSelectBuilder<T>> {


}

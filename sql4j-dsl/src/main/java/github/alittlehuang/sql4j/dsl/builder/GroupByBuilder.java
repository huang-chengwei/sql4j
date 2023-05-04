package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.util.Tuple;

public interface GroupByBuilder<T> extends
        GroupBy<T, GroupByBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        Mapper<GroupByBuilder<T>>,
        ResultBuilder<Tuple> {


}

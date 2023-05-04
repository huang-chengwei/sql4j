package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.util.Tuple;

public interface SelectBuilder<T> extends
        Where<T, SelectBuilder<T>>,
        OrderBy<T, SelectBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        AggregateSelect<T, AggregateSelectBuilder<T>>,
        ResultBuilder<Tuple>,
        Mapper<SelectBuilder<T>> {


}

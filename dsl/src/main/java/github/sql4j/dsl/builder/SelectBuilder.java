package github.sql4j.dsl.builder;

import github.sql4j.dsl.util.Tuple;

public interface SelectBuilder<T> extends
        Where<T, SelectBuilder<T>>,
        Sort<T, SelectBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        ResultBuilder<Tuple> {


}

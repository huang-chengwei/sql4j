package github.sql4j.dsl.builder;

public interface SelectBuilder<T> extends
        Where<T, SelectBuilder<T>>,
        Sort<T, SelectBuilder<T>>,
        Select<T, SelectBuilder<T>>,
        ResultBuilder<Object[]> {


}

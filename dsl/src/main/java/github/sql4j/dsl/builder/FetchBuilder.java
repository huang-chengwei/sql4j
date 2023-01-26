package github.sql4j.dsl.builder;

public interface FetchBuilder<T> extends
        PredicateCombiner<T, FetchBuilder<T>>,
        Fetch<T, FetchBuilder<T>>,
        Sort<T, FetchBuilder<T>>,
        ResultBuilder<T> {

}

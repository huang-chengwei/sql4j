package github.alittlehuang.sql4j.dsl.builder;

public interface FetchBuilder<T> extends
        PredicateCombiner<T, FetchBuilder<T>>,
        Fetch<T, FetchBuilder<T>>,
        OrderBy<T, FetchBuilder<T>>,
        ResultBuilder<T>,
        Mapper<FetchBuilder<T>> {

}

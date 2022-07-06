package github.sql4j.dsl.builder;

public interface Projection<T> {

    <R> ResultBuilder<R> projected(Class<R> projectionType);

}

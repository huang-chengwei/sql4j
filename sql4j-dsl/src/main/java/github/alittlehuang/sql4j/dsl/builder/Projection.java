package github.alittlehuang.sql4j.dsl.builder;

public interface Projection {

    <R> ResultBuilder<R> projected(Class<R> projectionType);

}

package github.alittlehuang.sql4j.dsl.support;

import github.alittlehuang.sql4j.dsl.support.builder.QuerySupport;

import java.util.function.UnaryOperator;

public interface Configure {

    Configure DEFAULT = new Configure() {
    };

    default <T> UnaryOperator<QuerySupport<T>> postProcessBeforeBuildQuerySpec() {
        return UnaryOperator.identity();
    }

}

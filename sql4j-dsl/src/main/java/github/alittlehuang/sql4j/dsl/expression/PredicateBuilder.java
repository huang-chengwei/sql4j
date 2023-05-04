package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.builder.PredicateCombiner;
import github.alittlehuang.sql4j.dsl.builder.Where;

public interface PredicateBuilder<T> extends PredicateCombiner<T, PredicateBuilder<T>>, Where<T, PredicateBuilder<T>>, Predicate<T> {

    @Override
    PredicateBuilder<T> not();

}

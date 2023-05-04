package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.support.builder.operator.Predicates;

/**
 * @see Predicates
 */
public interface Predicate<T> extends ExpressionSupplier {

    Predicate<T> not();

    Predicate<T> and(Predicate<T> predicate);

    Predicate<T> or(Predicate<T> predicate);

}

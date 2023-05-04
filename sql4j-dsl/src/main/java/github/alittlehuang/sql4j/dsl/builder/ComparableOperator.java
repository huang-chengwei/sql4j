package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.ComparableGetter;

public interface ComparableOperator<T, U extends Comparable<?>, BUILDER>
        extends PredicateOperator<T, U, BUILDER> {

    /**
     * greater than or equal to (>=)
     */
    BUILDER ge(U value);

    /**
     * greater than (>)
     */
    BUILDER gt(U value);

    /**
     * less than or equal to (<=)
     */
    BUILDER le(U value);

    /**
     * less than (<)
     */
    BUILDER lt(U value);

    BUILDER between(U a, U b);

    /**
     * greater than or equal to (>=)
     */
    BUILDER ge(ComparableGetter<T, U> value);

    /**
     * greater than (>)
     */
    BUILDER gt(ComparableGetter<T, U> value);

    /**
     * less than or equal to (<=)
     */
    BUILDER le(ComparableGetter<T, U> value);

    BUILDER between(ComparableGetter<T, U> a, ComparableGetter<T, U> b);

    /**
     * less than (<)
     */
    BUILDER lt(ComparableGetter<T, U> value);

    ComparableOperator<T, U, BUILDER> not();

}

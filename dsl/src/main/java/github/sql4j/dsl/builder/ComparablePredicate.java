package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.path.attribute.ComparableAttribute;

public interface ComparablePredicate<T, U extends Comparable<?>, BUILDER>
        extends BasePredicate<T, U, BUILDER> {

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
    BUILDER ge(Expression<U> value);

    /**
     * greater than (>)
     */
    BUILDER gt(Expression<U> value);

    /**
     * less than or equal to (<=)
     */
    BUILDER le(Expression<U> value);

    BUILDER between(Expression<U> a, Expression<U> b);

    /**
     * less than (<)
     */
    BUILDER lt(Expression<U> value);

    /**
     * greater than or equal to (>=)
     */
    BUILDER ge(ComparableAttribute<T, U> value);

    /**
     * greater than (>)
     */
    BUILDER gt(ComparableAttribute<T, U> value);

    /**
     * less than or equal to (<=)
     */
    BUILDER le(ComparableAttribute<T, U> value);

    BUILDER between(ComparableAttribute<T, U> a, ComparableAttribute<T, U> b);

    /**
     * less than (<)
     */
    BUILDER lt(ComparableAttribute<T, U> value);

    ComparablePredicate<T, U, BUILDER> not();

}

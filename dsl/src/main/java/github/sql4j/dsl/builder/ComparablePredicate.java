package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.SqlExpression;
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
    BUILDER ge(SqlExpression<U> value);

    /**
     * greater than (>)
     */
    BUILDER gt(SqlExpression<U> value);

    /**
     * less than or equal to (<=)
     */
    BUILDER le(SqlExpression<U> value);

    BUILDER between(SqlExpression<U> a, SqlExpression<U> b);

    /**
     * less than (<)
     */
    BUILDER lt(SqlExpression<U> value);

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

}

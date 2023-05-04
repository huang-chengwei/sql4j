package github.alittlehuang.sql4j.dsl.builder;

import java.util.Arrays;

public interface PredicateOperator<T, U, BUILDER> {

    BUILDER isNull();

    /**
     * equal
     */
    BUILDER eq(U value);

    /**
     * not equal
     */
    BUILDER ne(U value);

    @SuppressWarnings("unchecked")
    default BUILDER in(U... values) {
        return in(Arrays.asList(values));
    }

    BUILDER in(Iterable<? extends U> values);

    PredicateOperator<T, U, BUILDER> nullIf(U value);

    PredicateOperator<T, U, BUILDER> ifNull(U value);

    PredicateOperator<T, U, BUILDER> not();

}

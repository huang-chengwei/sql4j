package github.sql4j.dsl.builder;

import java.util.Arrays;
import java.util.Collection;

public interface BasePredicate<T, U, BUILDER> {

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

    BUILDER in(Collection<U> values);

    BasePredicate<T, U, BUILDER> nullIf(U value);

    BasePredicate<T, U, BUILDER> ifNull(U value);


}

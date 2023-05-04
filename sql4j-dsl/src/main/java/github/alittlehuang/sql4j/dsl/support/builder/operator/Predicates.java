package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.ComparableOperator;
import github.alittlehuang.sql4j.dsl.builder.NumberOperator;
import github.alittlehuang.sql4j.dsl.builder.PredicateOperator;
import github.alittlehuang.sql4j.dsl.builder.StringOperator;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.PredicateBuilder;
import github.alittlehuang.sql4j.dsl.expression.path.*;

public interface Predicates {

    static <T> PredicateBuilder<T> builder() {
        return new DefaultPredicateBuilder<>(Expression.TRUE);
    }

    static <T, R> PredicateOperator<T, R, PredicateBuilder<T>> where(ColumnGetter<T, R> reference) {
        return Predicates.<T>builder().where(reference);
    }

    static <T, R extends Number & Comparable<?>> NumberOperator<T, R, PredicateBuilder<T>> where(NumberGetter<T, R> reference) {
        return Predicates.<T>builder().where(reference);
    }

    static <T> StringOperator<T, PredicateBuilder<T>> where(StringGetter<T> reference) {
        return Predicates.<T>builder().where(reference);
    }

    static <T, R extends Comparable<?>> ComparableOperator<T, R, PredicateBuilder<T>> where(ComparableGetter<T, R> reference) {
        return Predicates.<T>builder().where(reference);
    }

    static <T> PredicateBuilder<T> where(BooleanGetter<T> reference) {
        return Predicates.<T>builder().where(reference).eq(true);
    }

}

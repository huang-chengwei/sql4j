package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.Predicate;
import github.alittlehuang.sql4j.dsl.expression.path.*;

public interface PredicateCombiner<T, BUILDER> {

    <R extends Persistable> PathBuilder<T, R, BUILDER> and(EntityGetter<T, R> attribute);

    <R extends Persistable> PathBuilder<T, R, BUILDER> or(EntityGetter<T, R> attribute);

    <R> PredicateOperator<T, R, BUILDER> and(ColumnGetter<T, R> attribute);

    <R> PredicateOperator<T, R, BUILDER> or(ColumnGetter<T, R> attribute);

    <R extends Number & Comparable<?>> NumberOperator<T, R, BUILDER> and(NumberGetter<T, R> attribute);

    <R extends Number & Comparable<?>> NumberOperator<T, R, BUILDER> or(NumberGetter<T, R> attribute);

    <R extends Comparable<?>> ComparableOperator<T, R, BUILDER> and(ComparableGetter<T, R> attribute);

    <R extends Comparable<?>> ComparableOperator<T, R, BUILDER> or(ComparableGetter<T, R> attribute);

    StringOperator<T, BUILDER> and(StringGetter<T> attribute);

    StringOperator<T, BUILDER> or(StringGetter<T> attribute);

    BUILDER and(Predicate<T> predicate);

    BUILDER or(Predicate<T> predicate);

}

package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.*;

public interface PathBuilder<T, U, BUILDER> {

    <R extends Persistable> PathBuilder<T, R, BUILDER> map(EntityGetter<U, R> column);

    <R extends Number & Comparable<?>> NumberOperator<T, R, BUILDER> map(NumberGetter<U, R> column);

    <R extends Comparable<?>> ComparableOperator<T, R, BUILDER> map(ComparableGetter<U, R> column);

    <R extends Comparable<?>> PredicateOperator<T, R, BUILDER> map(ColumnGetter<U, R> attribute);

    StringOperator<T, BUILDER> map(StringGetter<U> column);


}

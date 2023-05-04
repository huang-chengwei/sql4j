package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.Predicate;
import github.alittlehuang.sql4j.dsl.expression.path.*;

public interface Where<T, BUILDER> {

    <U extends Persistable> PathBuilder<T, U, BUILDER> where(EntityGetter<T, U> reference);

    <U> PredicateOperator<T, U, BUILDER> where(ColumnGetter<T, U> reference);

    <U extends Number & Comparable<?>> NumberOperator<T, U, BUILDER> where(NumberGetter<T, U> reference);

    <U extends Comparable<?>> ComparableOperator<T, U, BUILDER> where(ComparableGetter<T, U> reference);

    StringOperator<T, BUILDER> where(StringGetter<T> reference);

    BUILDER where(Predicate<T> predicate);

    Where<T, BUILDER> not();

}

package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.Predicate;
import github.sql4j.dsl.expression.path.Entity;
import github.sql4j.dsl.expression.path.PathBuilder;
import github.sql4j.dsl.expression.path.attribute.*;

public interface Where<T, BUILDER> {

    <U extends Entity> PathBuilder<T, U, BUILDER> where(EntityAttribute<T, U> column);

    <U> BasePredicate<T, U, BUILDER> where(Attribute<T, U> attribute);

    <U extends Number & Comparable<?>> NumberPredicate<T, U, BUILDER> where(NumberAttribute<T, U> column);

    <U extends Comparable<?>> ComparablePredicate<T, U, BUILDER> where(ComparableAttribute<T, U> column);

    StringPredicate<T, BUILDER> where(StringAttribute<T> column);

    BUILDER where(Predicate<T> predicate);

    Where<T, BUILDER> not();

}

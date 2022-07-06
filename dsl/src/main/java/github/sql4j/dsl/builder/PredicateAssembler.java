package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.Predicate;
import github.sql4j.dsl.expression.path.Entity;
import github.sql4j.dsl.expression.path.PathBuilder;
import github.sql4j.dsl.expression.path.attribute.*;

public interface PredicateAssembler<T, BUILDER> {

    <R extends Entity> PathBuilder<T, R, BUILDER> and(EntityAttribute<T, R> attribute);

    <R extends Entity> PathBuilder<T, R, BUILDER> or(EntityAttribute<T, R> attribute);

    <R extends Entity> PathBuilder<T, R, BUILDER> andNot(EntityAttribute<T, R> attribute);

    <R extends Entity> PathBuilder<T, R, BUILDER> orNot(EntityAttribute<T, R> attribute);

    <R> BasePredicate<T, R, BUILDER> and(Attribute<T, R> attribute);

    <R> BasePredicate<T, R, BUILDER> or(Attribute<T, R> attribute);

    <R> BasePredicate<T, R, BUILDER> andNot(Attribute<T, R> attribute);

    <R> BasePredicate<T, R, BUILDER> orNot(Attribute<T, R> attribute);

    <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> and(NumberAttribute<T, R> attribute);

    <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> or(NumberAttribute<T, R> attribute);

    <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> andNot(NumberAttribute<T, R> attribute);

    <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> orNot(NumberAttribute<T, R> attribute);

    <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> and(ComparableAttribute<T, R> attribute);

    <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> or(ComparableAttribute<T, R> attribute);

    <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> andNot(ComparableAttribute<T, R> attribute);

    <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> orNot(ComparableAttribute<T, R> attribute);

    StringPredicate<T, BUILDER> and(StringAttribute<T> attribute);

    StringPredicate<T, BUILDER> or(StringAttribute<T> attribute);

    StringPredicate<T, BUILDER> andNot(StringAttribute<T> attribute);

    StringPredicate<T, BUILDER> orNot(StringAttribute<T> attribute);

    BUILDER and(Predicate<T> predicate);

    BUILDER or(Predicate<T> predicate);

}

package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.builder.ComparablePredicate;
import github.sql4j.dsl.builder.NumberPredicate;
import github.sql4j.dsl.builder.BasePredicate;
import github.sql4j.dsl.builder.StringPredicate;
import github.sql4j.dsl.expression.path.attribute.*;

public interface PathBuilder<T, U, BUILDER> {

    <R extends Entity> PathBuilder<T, R, BUILDER> map(EntityAttribute<U, R> column);

    <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> map(NumberAttribute<U, R> column);

    <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> map(ComparableAttribute<U, R> column);

    <R extends Comparable<?>> BasePredicate<T, R, BUILDER> map(Attribute<U, R> attribute);

    StringPredicate<T, BUILDER> map(StringAttribute<U> column);


}

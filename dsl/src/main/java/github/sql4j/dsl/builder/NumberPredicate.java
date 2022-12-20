package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.path.attribute.NumberAttribute;

public interface NumberPredicate<T, U extends Number & Comparable<?>, BUILDER> extends ComparablePredicate<T, U, BUILDER> {

    NumberPredicate<T, U, BUILDER> add(U v);

    NumberPredicate<T, U, BUILDER> subtract(U v);

    NumberPredicate<T, U, BUILDER> multiply(U v);

    NumberPredicate<T, U, BUILDER> divide(U v);

    NumberPredicate<T, U, BUILDER> mod(U v);


    NumberPredicate<T, U, BUILDER> add(Expression<U> v);

    NumberPredicate<T, U, BUILDER> subtract(Expression<U> v);

    NumberPredicate<T, U, BUILDER> multiply(Expression<U> v);

    NumberPredicate<T, U, BUILDER> divide(Expression<U> v);

    NumberPredicate<T, U, BUILDER> mod(Expression<U> v);


    NumberPredicate<T, U, BUILDER> add(NumberAttribute<T, U> v);

    NumberPredicate<T, U, BUILDER> subtract(NumberAttribute<T, U> v);

    NumberPredicate<T, U, BUILDER> multiply(NumberAttribute<T, U> v);

    NumberPredicate<T, U, BUILDER> divide(NumberAttribute<T, U> v);

    NumberPredicate<T, U, BUILDER> mod(NumberAttribute<T, U> v);


}

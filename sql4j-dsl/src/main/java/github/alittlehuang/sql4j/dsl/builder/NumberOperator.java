package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.NumberGetter;

public interface NumberOperator<T, U extends Number & Comparable<?>, BUILDER> extends ComparableOperator<T, U, BUILDER> {

    NumberOperator<T, U, BUILDER> add(U v);

    NumberOperator<T, U, BUILDER> subtract(U v);

    NumberOperator<T, U, BUILDER> multiply(U v);

    NumberOperator<T, U, BUILDER> divide(U v);

    NumberOperator<T, U, BUILDER> mod(U v);


    NumberOperator<T, U, BUILDER> add(NumberGetter<T, U> v);

    NumberOperator<T, U, BUILDER> subtract(NumberGetter<T, U> v);

    NumberOperator<T, U, BUILDER> multiply(NumberGetter<T, U> v);

    NumberOperator<T, U, BUILDER> divide(NumberGetter<T, U> v);

    NumberOperator<T, U, BUILDER> mod(NumberGetter<T, U> v);


}

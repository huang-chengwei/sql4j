package github.sql4j.dsl.builder;

import github.sql4j.dsl.expression.path.attribute.ComparableAttribute;
import github.sql4j.dsl.expression.path.attribute.NumberAttribute;
import github.sql4j.dsl.expression.path.attribute.StringAttribute;

public interface Sort<T, BUILDER> {

    <U extends Number & Comparable<?>> SortAction<BUILDER> orderBy(NumberAttribute<T, U> column);

    <U extends Comparable<?>> SortAction<BUILDER> orderBy(ComparableAttribute<T, U> column);

    SortAction<BUILDER> orderBy(StringAttribute<T> column);


}

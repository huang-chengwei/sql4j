package github.alittlehuang.sql4j.dsl.builder;

import github.alittlehuang.sql4j.dsl.expression.path.ComparableGetter;
import github.alittlehuang.sql4j.dsl.expression.path.NumberGetter;
import github.alittlehuang.sql4j.dsl.expression.path.StringGetter;

public interface OrderBy<T, BUILDER> {

    <U extends Number & Comparable<?>> SortAction<BUILDER> orderBy(NumberGetter<T, U> column);

    <U extends Comparable<?>> SortAction<BUILDER> orderBy(ComparableGetter<T, U> column);

    SortAction<BUILDER> orderBy(StringGetter<T> column);


}

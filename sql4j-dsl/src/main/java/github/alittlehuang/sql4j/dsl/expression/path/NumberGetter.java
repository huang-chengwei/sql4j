package github.alittlehuang.sql4j.dsl.expression.path;


@FunctionalInterface
public interface NumberGetter<T, R extends Number & Comparable<?>> extends ColumnGetter<T, R> {

}

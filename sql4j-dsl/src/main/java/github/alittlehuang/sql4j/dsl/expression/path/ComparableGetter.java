package github.alittlehuang.sql4j.dsl.expression.path;

@FunctionalInterface
public interface ComparableGetter<T, U extends Comparable<?>> extends ColumnGetter<T, U> {

}

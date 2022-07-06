package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.expression.path.attribute.ComparableAttribute;

public class ComparablePath<T, R extends Comparable<?>>
        extends AttributePath<T, R>
        implements ComparableAttribute<T, R> {
    public ComparablePath(String... path) {
        super(path);
    }

}

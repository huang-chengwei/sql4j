package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.expression.path.attribute.NumberAttribute;

public class NumberPath<T, R extends Number & Comparable<?>>
        extends AttributePath<T, R>
        implements NumberAttribute<T, R> {
    public NumberPath(String... path) {
        super(path);
    }

}

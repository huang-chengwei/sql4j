package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.expression.path.attribute.BooleanAttribute;

public class BooleanPath<T>
        extends AttributePath<T, Boolean>
        implements BooleanAttribute<T> {
    public BooleanPath(String... path) {
        super(path);
    }
}

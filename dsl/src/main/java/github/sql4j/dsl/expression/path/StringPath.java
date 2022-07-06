package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.expression.path.attribute.StringAttribute;

public class StringPath<T>
        extends AttributePath<T, String>
        implements StringAttribute<T> {
    public StringPath(String... path) {
        super(path);
    }
}

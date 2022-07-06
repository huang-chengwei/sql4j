package github.sql4j.dsl.expression.path;

import github.sql4j.dsl.expression.path.attribute.EntityAttribute;

public class EntityPath<T, R extends Entity>
        extends AttributePath<T, R>
        implements EntityAttribute<T, R> {
    public EntityPath(String... path) {
        super(path);
    }

}

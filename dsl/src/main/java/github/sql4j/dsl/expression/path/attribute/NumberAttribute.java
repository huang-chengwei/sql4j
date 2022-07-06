package github.sql4j.dsl.expression.path.attribute;


@FunctionalInterface
public interface NumberAttribute<T, R extends Number & Comparable<?>> extends Attribute<T, R> {

}

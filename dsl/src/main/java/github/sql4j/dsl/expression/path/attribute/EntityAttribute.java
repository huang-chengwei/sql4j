package github.sql4j.dsl.expression.path.attribute;

import github.sql4j.dsl.expression.path.*;

@FunctionalInterface
public interface EntityAttribute<T, R extends Entity> extends Attribute<T, R> {

    static <T, R extends Entity> EntityAttribute<T, R> of(EntityAttribute<T, R> attribute) {
        return AttributePath.exchange(attribute);
    }

    default <V extends Entity> EntityPath<T, V> map(EntityAttribute<R, V> attribute) {
        throw new UnsupportedOperationException();
    }

    default <V extends Number & Comparable<?>> NumberPath<T, V> map(NumberAttribute<R, V> attribute) {
        throw new UnsupportedOperationException();
    }

    default <V extends Comparable<?>> ComparablePath<T, V> map(ComparableAttribute<R, V> attribute) {
        throw new UnsupportedOperationException();
    }

    default StringPath<T> map(StringAttribute<R> attribute) {
        throw new UnsupportedOperationException();
    }

    default BooleanPath<T> map(BooleanAttribute<R> attribute) {
        throw new UnsupportedOperationException();
    }

    default <V> Attribute<T, V> map(Attribute<R, V> attribute) {
        throw new UnsupportedOperationException();
    }


}

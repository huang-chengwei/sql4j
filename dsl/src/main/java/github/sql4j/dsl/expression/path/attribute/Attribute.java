package github.sql4j.dsl.expression.path.attribute;

import github.sql4j.dsl.expression.path.AttributePath;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface Attribute<T, R> extends Serializable {

    R map(T t);

    static <T, R> @NotNull Attribute<T, R> of(Attribute<T, R> attribute) {
        return AttributePath.exchange(attribute);
    }


    static <T, R extends Number & Comparable<?>> NumberAttribute<T, R> of(NumberAttribute<T, R> attribute) {
        return AttributePath.fromNumberAttributeBridge(attribute);
    }


    static <T> StringAttribute<T> of(StringAttribute<T> attribute) {
        return AttributePath.fromStringAttributeBridge(attribute);
    }


    static <T, R extends Comparable<?>> ComparableAttribute<T, R> of(ComparableAttribute<T, R> attribute) {
        return AttributePath.fromComparableAttributeBridge(attribute);
    }


}

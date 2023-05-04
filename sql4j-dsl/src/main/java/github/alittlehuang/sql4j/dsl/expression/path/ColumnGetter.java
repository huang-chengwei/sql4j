package github.alittlehuang.sql4j.dsl.expression.path;

import github.alittlehuang.sql4j.dsl.expression.ExpressionSupplier;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.support.builder.operator.GetterReferenceName;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@FunctionalInterface
public interface ColumnGetter<T, R> extends Serializable, ExpressionSupplier {

    /**
     *
     */
    R methodReference(T t);

    @Override
    default PathExpression expression() {
        return new PathExpression(GetterReferenceName.getPropertyName(this));
    }

    static <A, T, R> @NotNull ColumnGetter<T, R> of(ColumnGetter<T, R> attribute) {
        return attribute;
    }

    static <T, R extends Number & Comparable<?>> NumberGetter<T, R> of(NumberGetter<T, R> attribute) {
        return attribute;
    }

    static <T> StringGetter<T> of(StringGetter<T> attribute) {
        return attribute;
    }

    static <T, R extends Comparable<?>> ComparableGetter<T, R> of(ComparableGetter<T, R> attribute) {
        return attribute;
    }

    static <T, R extends Persistable> EntityGetter<T, R> of(EntityGetter<T, R> attribute) {
        return attribute;
    }


}

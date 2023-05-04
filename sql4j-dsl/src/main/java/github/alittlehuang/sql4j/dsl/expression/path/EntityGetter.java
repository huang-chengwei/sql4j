package github.alittlehuang.sql4j.dsl.expression.path;

import github.alittlehuang.sql4j.dsl.expression.PathExpression;

@FunctionalInterface
public interface EntityGetter<T, R extends Persistable> extends ColumnGetter<T, R> {

    default <V extends Persistable> EntityGetter<T, V> map(EntityGetter<R, V> reference) {
        return new EntityGetter<>() {
            @Override
            public V methodReference(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public PathExpression expression() {
                return EntityGetter.this.expression().join(reference.expression());
            }
        };
    }

    default <V extends Number & Comparable<?>> NumberGetter<T, V> map(NumberGetter<R, V> reference) {
        return new NumberGetter<>() {
            @Override
            public V methodReference(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public PathExpression expression() {
                return EntityGetter.this.expression().join(reference.expression());
            }
        };
    }

    default <V extends Comparable<?>> ComparableGetter<T, V> map(ComparableGetter<R, V> reference) {
        return new ComparableGetter<>() {
            @Override
            public V methodReference(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public PathExpression expression() {
                return EntityGetter.this.expression().join(reference.expression());
            }
        };
    }

    default StringGetter<T> map(StringGetter<R> attribute) {
        return new StringGetter<>() {
            @Override
            public String methodReference(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public PathExpression expression() {
                return EntityGetter.this.expression().join(attribute.expression());
            }
        };
    }

    default BooleanGetter<T> map(BooleanGetter<R> attribute) {
        return new BooleanGetter<>() {
            @Override
            public Boolean methodReference(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public PathExpression expression() {
                return EntityGetter.this.expression().join(attribute.expression());
            }
        };
    }

    default <V> ColumnGetter<T, V> map(ColumnGetter<R, V> reference) {
        return new ColumnGetter<>() {
            @Override
            public V methodReference(T t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public PathExpression expression() {
                return EntityGetter.this.expression().join(reference.expression());
            }
        };
    }

}

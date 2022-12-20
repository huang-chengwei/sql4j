package github.sql4j.dsl.expression;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PathExpression<T> implements Expression<T>, Iterable<String> {

    protected final String[] path;
    protected int length;

    public PathExpression(String... path) {
        if (path == null || path.length == 0) {
            throw new IllegalArgumentException();
        }
        this.path = path;
        this.length = this.path.length;
    }

    protected PathExpression(String[] path, int length) {
        this.path = path;
        this.length = length;
    }

    public PathExpression(PathExpression<?> path) {
        this.path = path.path;
        this.length = path.length;
    }

    @Override
    @NotNull
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length;
            }

            @Override
            public String next() {
                return path[index++];
            }

        };
    }

    public Stream<String> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public int size() {
        return length;
    }

    public String get(int index) {
        return path[index];
    }

    public PathExpression<?> parent() {
        int length = this.length - 1;
        if (length <= 0) {
            return null;
        }
        return new PathExpression<>(path, length);
    }

    public PathExpression<?> offset(int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        if (length > this.length) {
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        return new PathExpression<>(path, length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PathExpression)) {
            return false;
        }
        PathExpression<?> that = (PathExpression<?>) o;

        if (that.length != this.length) {
            return false;
        }
        Iterator<String> ia = this.iterator();
        Iterator<String> ib = that.iterator();
        while (ia.hasNext()) {
            if (!ib.hasNext()) {
                return false;
            }
            if (!Objects.equals(ia.next(), ib.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < length; i++) {
            String s = path[i];
            result = 31 * result + (s == null ? 0 : s.hashCode());
        }
        return result;
    }

    @Override
    public PathExpression<T> asPathExpression() {
        return this;
    }

    @SuppressWarnings("SameParameterValue")
    protected void arraycopy(int srcPos, String[] path, int destPos, int length) {
        if (length >= 0) {
            System.arraycopy(this.path, srcPos, path, destPos, length);
        }
    }

    @Override
    public String toString() {
        int iMax = length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(path[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public Expression.Type getType() {
        return Expression.Type.PATH;
    }

    public T getValue() {
        throw new UnsupportedOperationException();
    }

    public Operator getOperator() {
        throw new UnsupportedOperationException();
    }

    public List<? extends Expression<?>> getExpressions() {
        throw new UnsupportedOperationException();
    }

    public String[] toArray() {
        String[] array = new String[length];
        System.arraycopy(path, 0, array, 0, array.length);
        return array;
    }

}
